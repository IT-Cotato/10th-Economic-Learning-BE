package com.ripple.BE.news.application;

import static com.ripple.BE.news.exception.errorcode.NewsErrorCode.*;

import com.ripple.BE.news.domain.News;
import com.ripple.BE.news.domain.NewsScrap;
import com.ripple.BE.news.domain.type.NewsCategory;
import com.ripple.BE.news.domain.type.NewsSort;
import com.ripple.BE.news.dto.response.NewsPreviewListResponseDTO;
import com.ripple.BE.news.dto.response.NewsResponseDTO;
import com.ripple.BE.news.exception.NewsException;
import com.ripple.BE.news.persistence.NewsRepository;
import com.ripple.BE.news.persistence.NewsScrapRepository;
import com.ripple.BE.news.persistence.dto.NewsWithScrapDTO;
import com.ripple.BE.user.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class NewsService {

    private final NewsRepository newsRepository;
    private final NewsScrapRepository newsScrapRepository;

    private final AttendanceService attendanceService;

    private static final int PAGE_SIZE = 10;

    @Transactional(readOnly = true)
    public NewsPreviewListResponseDTO getNewsList(
            final int page, final NewsSort sort, final NewsCategory category, final long userId) {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        // 뉴스 조회
        Page<NewsWithScrapDTO> newsPage =
                category == null
                        ? newsRepository.findAll(pageable, sort, userId) // 전체 뉴스 조회
                        : newsRepository.findByCategory(category, sort, pageable, userId); // 특정 카테고리 뉴스 조회

        return NewsPreviewListResponseDTO.from(newsPage);
    }

    @Transactional
    public NewsResponseDTO getNews(final long id, final long userId) {
        News news =
                newsRepository.findByIdForUpdate(id).orElseThrow(() -> new NewsException(NEWS_NOT_FOUND));

        news = news.incrementViews(); // 조회수 증가
        newsRepository.save(news); // 변경된 뉴스 저장

        attendanceService.completeQuest(userId, "ARTICLE"); // 퀘스트 완료

        return NewsResponseDTO.from(news);
    }

    @Transactional
    public void addScrapToNews(final long newsId, final long userId) {

        newsRepository.findById(newsId).orElseThrow(() -> new NewsException(NEWS_NOT_FOUND));

        if (newsScrapRepository.existsByNewsIdAndUserId(newsId, userId)) {
            throw new NewsException(NEWS_SCRAP_ALREADY_EXIST);
        }

        NewsScrap newsScrap = NewsScrap.withoutId(userId, newsId);
        newsScrapRepository.save(newsScrap);
    }

    @Transactional
    public void removeScrapFromNews(final long newsId, final long userId) {
        NewsScrap newsScrap =
                newsScrapRepository
                        .findByNewsIdAndUserId(newsId, userId)
                        .orElseThrow(() -> new NewsException(NEWS_SCRAP_NOT_FOUND));

        newsScrapRepository.delete(newsScrap);
    }
}
