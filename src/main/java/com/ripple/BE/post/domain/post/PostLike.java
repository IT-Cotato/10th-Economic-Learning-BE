package com.ripple.BE.post.domain.post;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostLike {

    private final Long id;
    private final Long userId;
    private final Long postId;

    @Builder(access = AccessLevel.PRIVATE)
    private PostLike(Long id, Long userId, Long postId) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
    }

    public static PostLike withId(Long id, Long userId, Long postId) {
        return PostLike.builder().id(id).userId(userId).postId(postId).build();
    }

    public static PostLike withoutId(Long userId, Long postId) {
        return PostLike.builder().userId(userId).postId(postId).build();
    }
}
