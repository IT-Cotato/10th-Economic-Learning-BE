package com.ripple.BE.image.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Image {

    private final Long id;
    private final S3Info s3Info;
    private final Long postId;
    private final Long newsId;

    @Builder(access = AccessLevel.PRIVATE)
    public Image(Long id, S3Info s3Info, Long postId, Long newsId) {
        this.id = id;
        this.s3Info = s3Info;
        this.postId = postId;
        this.newsId = newsId;
    }

    public static Image withoutId(S3Info s3Info, Long postId, Long newsId) {
        return Image.builder().s3Info(s3Info).postId(postId).newsId(newsId).build();
    }

    public static Image withId(Long id, S3Info s3Info, Long postId, Long newsId) {
        return Image.builder().id(id).s3Info(s3Info).postId(postId).newsId(newsId).build();
    }

    public Image updatePostId(Long postId) {
        return Image.builder()
                .id(this.id)
                .s3Info(this.s3Info)
                .postId(postId)
                .newsId(this.newsId)
                .build();
    }
}
