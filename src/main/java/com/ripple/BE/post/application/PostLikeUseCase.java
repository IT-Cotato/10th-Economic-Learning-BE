package com.ripple.BE.post.application;

public interface PostLikeUseCase {
    void addLikeToPost(final long postId, final long userId);

    void removeLikeFromPost(final long postId, final long userId);
}
