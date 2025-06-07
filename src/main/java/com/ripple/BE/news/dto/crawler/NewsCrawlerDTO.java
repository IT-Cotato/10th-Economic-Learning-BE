package com.ripple.BE.news.dto.crawler;

import com.ripple.BE.global.utils.RelativeTimeFormatter;
import com.ripple.BE.news.domain.type.NewsCategory;
import java.time.LocalDateTime;

public record NewsCrawlerDTO(
        String title,
        String content,
        String publisher,
        String contentUrl,
        NewsCategory newsCategory,
        String pubDate) {
    public static NewsCrawlerDTO of(
            String title,
            String content,
            String publisher,
            String contentUrl,
            NewsCategory newsCategory,
            String pubDate) {
        return new NewsCrawlerDTO(title, content, publisher, contentUrl, newsCategory, pubDate);
    }

    public LocalDateTime parsedPubDate() {
        return RelativeTimeFormatter.parseRelativeTime(pubDate);
    }
}
