package com.ripple.BE.news.application;

import com.ripple.BE.news.application.crawler.NewsCrawler;
import com.ripple.BE.news.domain.News;
import com.ripple.BE.news.dto.crawler.NewsCrawlerDTO;
import com.ripple.BE.news.persistence.NewsRepository;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class NewsCrawlerService {

    private final NewsRepository newsRepository;

    @Async
    @Transactional
    public CompletableFuture<Void> crawl(NewsCrawler crawler) {

        List<NewsCrawlerDTO> newsList = crawler.crawl();
        List<News> filteredNews = filterOutDuplicateUrls(newsList);
        saveNewsBatch(filteredNews);

        return CompletableFuture.completedFuture(null);
    }

    public List<News> filterOutDuplicateUrls(List<NewsCrawlerDTO> newsList) {
        if (newsList.isEmpty()) {
            return List.of();
        }

        List<String> urls = newsList.stream().map(NewsCrawlerDTO::contentUrl).toList();
        List<String> existingUrls = newsRepository.findExistingUrls(urls);

        return newsList.stream()
                .filter(news -> !existingUrls.contains(news.contentUrl()))
                .map(
                        news ->
                                News.withoutId(
                                        news.title(),
                                        news.content(),
                                        news.publisher(),
                                        0L,
                                        news.contentUrl(),
                                        news.newsCategory(),
                                        news.parsedPubDate()))
                .toList();
    }

    private void saveNewsBatch(List<News> newsList) {
        if (newsList.isEmpty()) {
            return;
        }
        newsRepository.saveAllNews(newsList);
    }
}
