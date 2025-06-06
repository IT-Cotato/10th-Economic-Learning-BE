package com.ripple.BE.post.domain.post;

import com.ripple.BE.user.domain.User;
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

    public static PostScrap withId(Long id, User user, Post post) {
        return PostScrap.builder().id(id).userId(user.getId()).postId(post.getId()).build();
    }

    public static PostScrap withoutId(User user, Post post) {
        return PostScrap.builder().userId(user.getId()).postId(post.getId()).build();
    }
}
