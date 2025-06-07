package com.ripple.BE.news.persistence.jpa.entity;

import com.ripple.BE.global.entity.BaseJpaEntity;
import com.ripple.BE.news.domain.NewsScrap;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "news_scraps")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NewsScrapJpaEntity extends BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "news_id", nullable = false)
    private Long newsId;

    @Builder(access = AccessLevel.PRIVATE)
    private NewsScrapJpaEntity(Long id, Long userId, Long newsId) {
        this.id = id;
        this.userId = userId;
        this.newsId = newsId;
    }

    public static NewsScrapJpaEntity from(NewsScrap newsScrap) {
        return NewsScrapJpaEntity.builder()
                .id(newsScrap.getId())
                .userId(newsScrap.getUserId())
                .newsId(newsScrap.getNewsId())
                .build();
    }

    public NewsScrap toModel() {
        return NewsScrap.withId(this.id, this.userId, this.newsId);
    }
}
