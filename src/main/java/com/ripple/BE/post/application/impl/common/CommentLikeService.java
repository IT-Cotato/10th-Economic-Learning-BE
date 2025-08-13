package com.ripple.BE.post.application.impl.common;

import static com.ripple.BE.post.exception.errorcode.PostErrorCode.*;

import com.ripple.BE.post.application.CommentLikeUseCase;
import com.ripple.BE.post.application.cache.PostCacheEvictionService;
import com.ripple.BE.post.domain.comment.Comment;
import com.ripple.BE.post.domain.comment.CommentLike;
import com.ripple.BE.post.domain.post.Post;
import com.ripple.BE.post.exception.PostException;
import com.ripple.BE.post.persistence.CommentLikeRepository;
import com.ripple.BE.post.persistence.CommentRepository;
import com.ripple.BE.post.persistence.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentLikeService implements CommentLikeUseCase {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final PostCacheEvictionService cacheEvictionService;

    @Override
    public void addLikeToComment(final long commentId, final long userId, final long postId) {

        Post post =
                postRepository.findById(postId).orElseThrow(() -> new PostException(POST_NOT_FOUND));
        Comment comment = findCommentByIdForUpdate(commentId);

        if (commentLikeRepository.existsByCommentIdAndUserId(commentId, userId)) {
            throw new PostException(LIKE_ALREADY_EXISTS);
        }
        comment.validateUpdatableBy(userId, post.getId());

        CommentLike commentLike = CommentLike.withoutId(userId, commentId);
        commentLikeRepository.save(commentLike);

        commentRepository.save(comment.increaseLikeCount());

        // 댓글 좋아요 증가 후 캐시 무효화
        cacheEvictionService.evictPostCachesIfContained(post);
    }

    @Override
    public void removeLikeFromComment(final long commentId, final long userId, final long postId) {

        Post post =
                postRepository.findById(postId).orElseThrow(() -> new PostException(POST_NOT_FOUND));
        Comment comment = findCommentByIdForUpdate(commentId);

        CommentLike commentLike =
                commentLikeRepository
                        .findByCommentIdAndUserId(commentId, userId)
                        .orElseThrow(() -> new PostException(LIKE_NOT_FOUND));

        comment.validateUpdatableBy(userId, post.getId());
        commentLikeRepository.delete(commentLike);

        commentRepository.save(comment.decreaseLikeCount());

        // 댓글 좋아요 감소 후 캐시 무효화
        cacheEvictionService.evictPostCachesIfContained(post);
    }

    private Comment findCommentByIdForUpdate(final long id) {
        return commentRepository
                .findByIdForUpdate(id)
                .orElseThrow(() -> new PostException(COMMENT_NOT_FOUND));
    }
}
