package com.ripple.BE.news.persistence.dto;

import com.ripple.BE.news.domain.type.NewsCategory;
import java.time.LocalDateTime;

public record NewsWithScrapDTO(
        Long id,
        String title,
        String content,
        String publisher,
        String url,
        NewsCategory category,
        Long views,
        LocalDateTime pubDate,
        boolean isScrapped) {}
