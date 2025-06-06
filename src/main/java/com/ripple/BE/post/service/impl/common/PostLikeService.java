package com.ripple.BE.post.service.impl.common;

import static com.ripple.BE.post.exception.errorcode.PostErrorCode.*;

import com.ripple.BE.notification.service.NotificationService;
import com.ripple.BE.post.domain.post.Post;
import com.ripple.BE.post.domain.post.PostLike;
import com.ripple.BE.post.exception.PostException;
import com.ripple.BE.post.persistence.PostLikeRepository;
import com.ripple.BE.post.persistence.PostRepository;
import com.ripple.BE.post.service.PostLikeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostLikeService implements PostLikeUseCase {

    private static final int POPULAR_POST_LIKE_COUNT = 10;

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    private final NotificationService notificationService;

    @Override
    public void addLikeToPost(final long postId, final long userId) {

        Post post = findPostByIdForUpdate(postId);

        if (postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
            throw new PostException(LIKE_ALREADY_EXISTS);
        }

        PostLike postLike = PostLike.withoutId(userId, post.getId());
        postLikeRepository.save(postLike);

        postRepository.save(post.increaseLikeCount());

        if (post.getLikeCount() == POPULAR_POST_LIKE_COUNT) {
            // notificationService.createPopularNotification(post);
            // 알람 로직 수정 후 주석 해제
        }
    }

    @Override
    public void removeLikeFromPost(final long postId, final long userId) {

        Post post = findPostByIdForUpdate(postId);

        PostLike postLike =
                postLikeRepository
                        .findByPostIdAndUserId(postId, userId)
                        .orElseThrow(() -> new PostException(LIKE_NOT_FOUND));
        postLikeRepository.delete(postLike);

        postRepository.save(post.decreaseLikeCount());
    }

    private Post findPostByIdForUpdate(final long id) {
        return postRepository
                .findByIdForUpdate(id)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));
    }
}
