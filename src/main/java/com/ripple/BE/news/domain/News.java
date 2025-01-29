package com.ripple.BE.news.domain;

import com.ripple.BE.global.entity.BaseEntity;
import com.ripple.BE.image.domain.Image;
import com.ripple.BE.news.domain.type.NewsCategory;
import com.ripple.BE.news.dto.NewsDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "news")
@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class News extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "views")
    private long views = 0L;

    @Column(name = "url")
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private NewsCategory category;

    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> imageList = new ArrayList<>();

    @Setter @Transient private Boolean isScrapped;

    public static News toNewsEntity(NewsDTO newsDTO) {
        return News.builder()
                .title(newsDTO.title())
                .content(newsDTO.content())
                .publisher(newsDTO.publisher())
                .views(0L)
                .url(newsDTO.url())
                .category(newsDTO.category())
                .build();
    }

    public void increaseViews() {
        this.views++;
    }
}
