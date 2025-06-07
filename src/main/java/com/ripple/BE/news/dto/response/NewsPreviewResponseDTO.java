package com.ripple.BE.news.dto.response;

import com.ripple.BE.global.utils.RelativeTimeFormatter;
import com.ripple.BE.news.persistence.dto.NewsWithScrapDTO;

public record NewsPreviewResponseDTO(
        Long id,
        String title,
        String content,
        String publisher,
        long views,
        String url,
        String category,
        Boolean isScraped,
        String createdDate) {

    public static NewsPreviewResponseDTO from(NewsWithScrapDTO newsWithScrapDTO) {
        return new NewsPreviewResponseDTO(
                newsWithScrapDTO.id(),
                newsWithScrapDTO.title(),
                newsWithScrapDTO.content(),
                newsWithScrapDTO.publisher(),
                newsWithScrapDTO.views(),
                newsWithScrapDTO.url(),
                newsWithScrapDTO.category().toString(),
                newsWithScrapDTO.isScrapped(),
                RelativeTimeFormatter.formatRelativeTime(newsWithScrapDTO.pubDate()));
    }
}
