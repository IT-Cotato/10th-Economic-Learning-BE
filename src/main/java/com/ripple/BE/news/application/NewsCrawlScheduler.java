package com.ripple.BE.news.application;

import com.ripple.BE.news.application.crawler.NewsCrawler;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NewsCrawlScheduler {

    private final NewsCrawlerService newsCrawlerService;
    private final List<NewsCrawler> crawlers;

    // 1시간 마다 크롤링을 수행하는 스케줄러
    @Scheduled(fixedRate = 3600000)
    public void fetchAndSaveAllNewsAsync() {
        List<CompletableFuture<Void>> futures =
                crawlers.stream().map(newsCrawlerService::crawl).toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join(); // 모든 크롤링이 끝날 때까지 대기
    }
}
