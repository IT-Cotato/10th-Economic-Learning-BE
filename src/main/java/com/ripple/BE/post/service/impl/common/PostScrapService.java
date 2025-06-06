package com.ripple.BE.post.service.impl.common;

import static com.ripple.BE.post.exception.errorcode.PostErrorCode.*;

import com.ripple.BE.post.domain.post.Post;
import com.ripple.BE.post.domain.post.PostScrap;
import com.ripple.BE.post.exception.PostException;
import com.ripple.BE.post.persistence.PostRepository;
import com.ripple.BE.post.persistence.PostScrapRepository;
import com.ripple.BE.post.service.PostScrapUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostScrapService implements PostScrapUseCase {

    private final PostRepository postRepository;
    private final PostScrapRepository postScrapRepository;

    @Override
    public void addScrapToPost(final long postId, final long userId) {

        Post post = findPostByIdForUpdate(postId);

        if (postScrapRepository.existsByPostIdAndUserId(postId, userId)) {
            throw new PostException(SCRAP_ALREADY_EXISTS);
        }

        PostScrap postScrap = PostScrap.withoutId(userId, post.getId());
        postScrapRepository.save(postScrap);

        postRepository.save(post.increaseScrapCount());
    }

    @Override
    public void removeScrapFromPost(final long postId, final long userId) {
        Post post = findPostByIdForUpdate(postId);

        PostScrap postScrap =
                postScrapRepository
                        .findByPostIdAndUserId(postId, userId)
                        .orElseThrow(() -> new PostException(SCRAP_NOT_FOUND));

        postScrapRepository.delete(postScrap);

        postRepository.save(post.decreaseScrapCount());
    }

    private Post findPostByIdForUpdate(final long id) {
        return postRepository
                .findByIdForUpdate(id)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));
    }
}
