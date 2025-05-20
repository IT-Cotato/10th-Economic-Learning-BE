package com.ripple.BE.post.service;

public interface PostScrapUseCase {

    void addScrapToPost(final long postId, final long userId);

    void removeScrapFromPost(final long postId, final long userId);
}
