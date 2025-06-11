package com.ripple.BE.news.persistence.jpa.entity;

import com.ripple.BE.global.entity.BaseJpaEntity;
import com.ripple.BE.news.domain.News;
import com.ripple.BE.news.domain.type.NewsCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "news")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NewsJpaEntity extends BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "views", nullable = false)
    private long views;

    @Column(name = "url")
    private String url;

    @Column(name = "pub_date")
    private LocalDateTime pubDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private NewsCategory category;

    @Builder(access = AccessLevel.PRIVATE)
    private NewsJpaEntity(
            Long id,
            String title,
            String content,
            String publisher,
            long views,
            String url,
            LocalDateTime pubDate,
            NewsCategory category) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.publisher = publisher;
        this.views = views;
        this.url = url;
        this.pubDate = pubDate;
        this.category = category;
    }

    public static NewsJpaEntity from(News news) {

        return NewsJpaEntity.builder()
                .id(news.getId())
                .title(news.getTitle())
                .content(news.getContent())
                .publisher(news.getPublisher())
                .views(news.getViews())
                .url(news.getUrl())
                .category(news.getCategory())
                .pubDate(news.getPubDate())
                .build();
    }

    public News toModel() {
        return News.withId(
                this.id,
                this.title,
                this.content,
                this.publisher,
                this.views,
                this.url,
                this.category,
                this.pubDate);
    }
}
