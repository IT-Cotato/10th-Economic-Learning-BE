package com.ripple.BE.news.domain;

import com.ripple.BE.news.domain.type.NewsCategory;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class News {

    private final Long id;
    private final String title;
    private final String content;
    private final String publisher;
    private final long views;
    private final String url;
    private final NewsCategory category;
    private final LocalDateTime pubDate;

    @Builder(access = AccessLevel.PRIVATE)
    private News(
            Long id,
            String title,
            String content,
            String publisher,
            long views,
            String url,
            NewsCategory category,
            LocalDateTime pubDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.publisher = publisher;
        this.views = views;
        this.url = url;
        this.category = category;
        this.pubDate = pubDate;
    }

    public static News withId(
            Long id,
            String title,
            String content,
            String publisher,
            long views,
            String url,
            NewsCategory category,
            LocalDateTime publishedDate) {
        return News.builder()
                .id(id)
                .title(title)
                .content(content)
                .publisher(publisher)
                .views(views)
                .url(url)
                .category(category)
                .pubDate(publishedDate)
                .build();
    }

    public static News withoutId(
            String title,
            String content,
            String publisher,
            long views,
            String url,
            NewsCategory category,
            LocalDateTime publishedDate) {
        return News.builder()
                .title(title)
                .content(content)
                .publisher(publisher)
                .views(views)
                .url(url)
                .category(category)
                .pubDate(publishedDate)
                .build();
    }

    public News incrementViews() {
        return News.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .publisher(this.publisher)
                .views(this.views + 1)
                .url(this.url)
                .category(this.category)
                .pubDate(this.pubDate)
                .build();
    }
}
