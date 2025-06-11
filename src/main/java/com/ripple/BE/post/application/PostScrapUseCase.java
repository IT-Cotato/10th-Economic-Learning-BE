package com.ripple.BE.post.application;

public interface PostScrapUseCase {

    void addScrapToPost(final long postId, final long userId);

    void removeScrapFromPost(final long postId, final long userId);
}
