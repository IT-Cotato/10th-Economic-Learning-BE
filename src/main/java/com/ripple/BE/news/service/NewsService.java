package com.ripple.BE.news.service;

import static com.ripple.BE.news.exception.errorcode.NewsErrorCode.*;

import com.ripple.BE.news.crawler.NewsCrawler;
import com.ripple.BE.news.domain.News;
import com.ripple.BE.news.domain.NewsScrap;
import com.ripple.BE.news.domain.type.NewsCategory;
import com.ripple.BE.news.domain.type.NewsSort;
import com.ripple.BE.news.dto.NewsDTO;
import com.ripple.BE.news.dto.NewsListDTO;
import com.ripple.BE.news.exception.NewsException;
import com.ripple.BE.news.repository.news.NewsJdbcRepository;
import com.ripple.BE.news.repository.news.NewsRepository;
import com.ripple.BE.news.repository.newscrap.NewsScrapRepository;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class NewsService {

    private final NewsRepository newsRepository;
    private final NewsScrapRepository newsScrapRepository;
    private final NewsJdbcRepository newsJdbcRepository;

    private final UserService userService;
    private final List<NewsCrawler> crawlers;

    private static final int PAGE_SIZE = 10;

    @Cacheable(
            value = "newsList",
            key =
                    "#page + (#sort != null ? #sort.toString() : '') + (#category != null ? #category.toString() : '')")
    @Transactional(readOnly = true)
    public NewsListDTO getNewsList(
            final int page, final NewsSort sort, final NewsCategory category, final long userId) {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        // 게시글 조회 (타입에 따른 필터링)
        Page<News> newsPage =
                category == null
                        ? newsRepository.findAll(pageable, sort, userId) // 일반 게시글 조회
                        : newsRepository.findByCategory(category, sort, pageable, userId); // 특정 타입 게시글 조회

        return NewsListDTO.toNewsListDTO(newsPage);
    }

    @Transactional
    public NewsDTO getNews(final long id) {
        News news =
                newsRepository.findByIdForUpdate(id).orElseThrow(() -> new NewsException(NEWS_NOT_FOUND));

        news.increaseViews(); // 조회수 증가

        return NewsDTO.toNewsDTO(news);
    }

    @Transactional
    public void addScrapToNews(final long newsId, final long userId) {

        News news =
                newsRepository.findById(newsId).orElseThrow(() -> new NewsException(NEWS_NOT_FOUND));
        User user = userService.findUserById(userId);

        if (newsScrapRepository.existsByNewsIdAndUserId(newsId, userId)) {
            throw new NewsException(NEWS_SCRAP_ALREADY_EXIST);
        }

        NewsScrap newsScrap = NewsScrap.toNewsScrapEntity();
        newsScrap.setUser(user);
        newsScrap.setNews(news);
    }

    @Transactional
    public void removeScrapFromNews(final long newsId, final long userId) {
        NewsScrap newsScrap =
                newsScrapRepository
                        .findByNewsIdAndUserId(newsId, userId)
                        .orElseThrow(() -> new NewsException(NEWS_SCRAP_NOT_FOUND));

        newsScrapRepository.delete(newsScrap);
    }

    // 하루 한 번 뉴스 크롤링, 실제 배포시는 짧은 주기로 변경 필요
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void fetchAndSaveAllNews() {
        for (NewsCrawler crawler : crawlers) {
            List<NewsDTO> newsList = crawler.crawl();

            List<News> filteredNews = filterOutDuplicateUrls(newsList);

            saveNewsBatch(filteredNews);
        }
    }

    private List<News> filterOutDuplicateUrls(List<NewsDTO> newsList) {
        if (newsList.isEmpty()) {
            return List.of();
        }

        List<String> urls = newsList.stream().map(NewsDTO::url).toList();
        List<String> existingUrls = newsJdbcRepository.findExistingUrls(urls);

        return newsList.stream()
                .filter(dto -> !existingUrls.contains(dto.url()))
                .map(News::toNewsEntity)
                .toList();
    }

    private void saveNewsBatch(List<News> newsList) {
        if (newsList.isEmpty()) {
            return;
        }

        newsJdbcRepository.saveAllNewsByJdbcTemplate(newsList);
    }
}
