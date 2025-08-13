package com.ripple.BE.post.application.impl.common;

import static com.ripple.BE.post.exception.errorcode.PostErrorCode.*;

import com.ripple.BE.notification.application.event.CommentCreatedEvent;
import com.ripple.BE.notification.application.event.ReplyCommentCreatedEvent;
import com.ripple.BE.post.application.CommentCommandUseCase;
import com.ripple.BE.post.application.cache.PostCacheEvictionService;
import com.ripple.BE.post.domain.comment.Comment;
import com.ripple.BE.post.domain.post.Post;
import com.ripple.BE.post.exception.PostException;
import com.ripple.BE.post.persistence.CommentLikeRepository;
import com.ripple.BE.post.persistence.CommentRepository;
import com.ripple.BE.post.persistence.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentCommandService implements CommentCommandUseCase {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    private final ApplicationEventPublisher eventPublisher;
    private final PostCacheEvictionService cacheEvictionService;

    @Override
    public void addCommentToPost(final long userId, final long postId, final String content) {

        Post post = findPostByIdForUpdate(postId);

        Comment comment = Comment.withoutId(content, userId, postId, null);
        commentRepository.save(comment);

        post = postRepository.save(post.increaseCommentCount());

        // 댓글 추가 후 스마트 캐시 무효화
        cacheEvictionService.evictPostCachesIfContained(post);

        eventPublisher.publishEvent(new CommentCreatedEvent(post, comment));
    }

    @Override
    public void addReplyToComment(
            final long userId, final long postId, final long commentId, final String content) {

        Post post = findPostByIdForUpdate(postId);
        Comment parentComment = findCommentByIdForUpdate(commentId);

        parentComment.validateReplyable(post.getId());

        Comment comment = Comment.withoutId(content, userId, post.getId(), parentComment.getId());
        commentRepository.save(comment);
        commentRepository.save(parentComment.increaseReplyCount());

        post = postRepository.save(post.increaseCommentCount());

        // 답글 추가 후 캐시 무효화
        cacheEvictionService.evictPostCachesIfContained(post);

        eventPublisher.publishEvent(new ReplyCommentCreatedEvent(post, comment, parentComment));
    }

    @Override
    public void removeCommentFromPost(final long userId, final long postId, final long commentId) {
        Post post = findPostByIdForUpdate(postId);
        Comment comment = findCommentByIdForUpdate(commentId);

        comment.validateDeletableBy(userId, post.getId());

        post = postRepository.save(post.decreaseCommentCount());

        // 댓글 삭제 후 캐시 무효화
        cacheEvictionService.evictPostCachesIfContained(post);

        if (comment.isRoot()) {
            handleRootComment(comment);
        } else {
            handleChildComment(comment, comment.getParentCommentId());
        }
    }

    private void handleRootComment(Comment comment) {
        if (comment.hasNoChildren()) {
            commentLikeRepository.deleteAllByCommentId(comment.getId());
            commentRepository.delete(comment);
        } else {
            commentRepository.save(comment.softDeleteAsRoot());
        }
    }

    private void handleChildComment(Comment comment, Long parentCommentId) {
        Comment parent =
                commentRepository
                        .findByIdForUpdate(parentCommentId)
                        .orElseThrow(() -> new PostException(COMMENT_NOT_FOUND));

        commentLikeRepository.deleteAllByCommentId(comment.getId());
        commentRepository.delete(comment);

        commentRepository.save(parent.decreaseReplyCount());

        if (parent.isDeleted() && parent.hasNoChildren()) {
            commentLikeRepository.deleteAllByCommentId(parent.getId());
            commentRepository.delete(parent);
        }
    }

    @Override
    public void updateComment(
            final long userId, final long postId, final long commentId, final String content) {
        Post post = findPostByIdForUpdate(postId);
        Comment comment = findCommentByIdForUpdate(commentId);

        comment.validateUpdatableBy(userId, post.getId());

        commentRepository.save(comment.updateContent(content));
    }

    private Post findPostByIdForUpdate(final long id) {
        return postRepository
                .findByIdForUpdate(id)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));
    }

    private Comment findCommentByIdForUpdate(final long id) {
        return commentRepository
                .findByIdForUpdate(id)
                .orElseThrow(() -> new PostException(COMMENT_NOT_FOUND));
    }
}
