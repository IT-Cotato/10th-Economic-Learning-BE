package com.ripple.BE.news.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class NewsScrap {

    private final Long id;
    private final Long userId;
    private final Long newsId;

    @Builder(access = AccessLevel.PRIVATE)
    private NewsScrap(Long id, Long userId, Long newsId) {
        this.id = id;
        this.userId = userId;
        this.newsId = newsId;
    }

    public static NewsScrap withId(Long id, Long userId, Long newsId) {
        return NewsScrap.builder().id(id).userId(userId).newsId(newsId).build();
    }

    public static NewsScrap withoutId(Long userId, Long newsId) {
        return NewsScrap.builder().userId(userId).newsId(newsId).build();
    }
}
