package com.ripple.BE.post.service;

public interface PostLikeUseCase {
    void addLikeToPost(final long postId, final long userId);

    void removeLikeFromPost(final long postId, final long userId);
}
