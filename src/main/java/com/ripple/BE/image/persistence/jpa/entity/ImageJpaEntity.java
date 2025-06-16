package com.ripple.BE.image.persistence.jpa.entity;

import com.ripple.BE.global.entity.BaseJpaEntity;
import com.ripple.BE.image.domain.Image;
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

@Table(name = "images")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageJpaEntity extends BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "s3_info")
    private S3InfoJpaEntity s3Info;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "news_id")
    private Long newsId;

    @Builder(access = AccessLevel.PRIVATE)
    public ImageJpaEntity(Long id, S3InfoJpaEntity s3Info, Long postId, Long newsId) {
        this.id = id;
        this.s3Info = s3Info;
        this.postId = postId;
        this.newsId = newsId;
    }

    public static ImageJpaEntity from(Image image) {
        return ImageJpaEntity.builder()
                .id(image.getId())
                .s3Info(S3InfoJpaEntity.from(image.getS3Info()))
                .postId(image.getPostId())
                .newsId(image.getNewsId())
                .build();
    }

    public Image toModel() {
        return Image.withId(this.id, this.s3Info.toModel(), this.postId, this.newsId);
    }
}
