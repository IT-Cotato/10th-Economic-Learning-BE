package com.ripple.BE.news.service;

import com.ripple.BE.news.crawler.NewsCrawler;
import com.ripple.BE.news.domain.News;
import com.ripple.BE.news.dto.NewsDTO;
import com.ripple.BE.news.repository.news.NewsJdbcRepository;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class NewsCrawlerService {

    private final NewsJdbcRepository newsJdbcRepository;

    @Async
    @Transactional
    public CompletableFuture<Void> crawl(NewsCrawler crawler) {

        List<NewsDTO> newsList = crawler.crawl();
        List<News> filteredNews = filterOutDuplicateUrls(newsList);
        saveNewsBatch(filteredNews);

        return CompletableFuture.completedFuture(null);
    }

    public List<News> filterOutDuplicateUrls(List<NewsDTO> newsList) {
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

    public void saveNewsBatch(List<News> newsList) {
        if (newsList.isEmpty()) {
            return;
        }

        newsJdbcRepository.saveAllNewsByJdbcTemplate(newsList);
    }
}
