package com.ripple.BE.post.domain.post;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostScrap {

    private final Long id;
    private final Long userId;
    private final Long postId;

    @Builder(access = AccessLevel.PRIVATE)
    private PostScrap(Long id, Long userId, Long postId) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
    }

    public static PostScrap withId(Long id, Long userId, Long postId) {
        return PostScrap.builder().id(id).userId(userId).postId(postId).build();
    }

    public static PostScrap withoutId(Long userId, Long postId) {
        return PostScrap.builder().userId(userId).postId(postId).build();
    }
}
