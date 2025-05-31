package com.ripple.BE.post.application;

public interface CommentLikeUseCase {
    void addLikeToComment(final long commentId, final long userId, final long postId);

    void removeLikeFromComment(final long commentId, final long userId, final long postId);
}
