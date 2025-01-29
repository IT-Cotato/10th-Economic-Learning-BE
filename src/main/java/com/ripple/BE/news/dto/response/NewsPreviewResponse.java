package com.ripple.BE.news.dto.response;

import com.ripple.BE.global.utils.RelativeTimeFormatter;
import com.ripple.BE.news.dto.NewsDTO;

public record NewsPreviewResponse(
        Long id,
        String title,
        String content,
        String publisher,
        long views,
        String url,
        String category,
        Boolean isScraped,
        String createdDate) {

    public static NewsPreviewResponse toNewsPreviewResponse(NewsDTO newDTO) {
        return new NewsPreviewResponse(
                newDTO.id(),
                newDTO.title(),
                newDTO.content(),
                newDTO.publisher(),
                newDTO.views(),
                newDTO.url(),
                newDTO.category().toString(),
                newDTO.isScraped(),
                RelativeTimeFormatter.formatRelativeTime(newDTO.createdDate()));
    }
}
