package com.ripple.BE.post.domain.comment;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentLike {

    private final Long id;
    private final Long userId;
    private final Long commentId;

    @Builder(access = AccessLevel.PRIVATE)
    private CommentLike(Long id, Long userId, Long commentId) {
        this.id = id;
        this.userId = userId;
        this.commentId = commentId;
    }

    public static CommentLike withId(Long id, Long userId, Long commentId) {
        return CommentLike.builder().id(id).userId(userId).commentId(commentId).build();
    }

    public static CommentLike withoutId(Long userId, Long commentId) {
        return CommentLike.builder().userId(userId).commentId(commentId).build();
    }
}
