package com.ripple.BE.news.dto.response;

import com.ripple.BE.global.utils.RelativeTimeFormatter;
import com.ripple.BE.news.domain.News;

public record NewsResponseDTO(
        Long id,
        String title,
        String content,
        String publisher,
        long views,
        String url,
        String category,
        String createdDate) {

    public static NewsResponseDTO from(News news) {
        return new NewsResponseDTO(
                news.getId(),
                news.getTitle(),
                news.getContent(),
                news.getPublisher(),
                news.getViews(),
                news.getUrl(),
                news.getCategory().toString(),
                RelativeTimeFormatter.formatRelativeTime(news.getPubDate()));
    }
}
