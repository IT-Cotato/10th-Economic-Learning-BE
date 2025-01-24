package com.ripple.BE.news.dto.response;

import com.ripple.BE.global.utils.RelativeTimeFormatter;
import com.ripple.BE.news.dto.NewsDTO;

public record NewsResponse(
        Long id,
        String title,
        String content,
        String publisher,
        long views,
        String url,
        String category,
        String createdDate) {

    public static NewsResponse toNewsResponse(NewsDTO newDTO) {
        return new NewsResponse(
                newDTO.id(),
                newDTO.title(),
                newDTO.content(),
                newDTO.publisher(),
                newDTO.views(),
                newDTO.url(),
                newDTO.category().toString(),
                RelativeTimeFormatter.formatRelativeTime(newDTO.createdDate()));
    }
}
