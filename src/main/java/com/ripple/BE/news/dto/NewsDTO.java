package com.ripple.BE.news.dto;

import com.ripple.BE.image.dto.ImageListDTO;
import com.ripple.BE.news.domain.News;
import com.ripple.BE.news.domain.type.NewsCategory;
import java.time.LocalDateTime;

public record NewsDTO(
        Long id,
        String title,
        String content,
        String publisher,
        String url,
        Long views,
        NewsCategory category,
        LocalDateTime createdDate,
        ImageListDTO imageList) {

    public static NewsDTO toNewsDTO(final News news) {
        return new NewsDTO(
                news.getId(),
                news.getTitle(),
                news.getContent(),
                news.getPublisher(),
                news.getUrl(),
                news.getViews(),
                news.getCategory(),
                news.getCreatedDate(),
                ImageListDTO.toImageListDTO(news.getImageList()));
    }

    public static NewsDTO toNewsDTO(
            final String title,
            final String content,
            final String publisher,
            final String url,
            final NewsCategory category) {
        return new NewsDTO(null, title, content, publisher, url, null, category, null, null);
    }
}
