package com.ripple.BE.post.application.impl.common;

import static com.ripple.BE.post.exception.errorcode.PostErrorCode.*;

import com.ripple.BE.notification.service.NotificationService;
import com.ripple.BE.post.application.CommentCommandUseCase;
import com.ripple.BE.post.domain.comment.Comment;
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
public class CommentCommandService implements CommentCommandUseCase {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    private final NotificationService notificationService;

    @Override
    public void addCommentToPost(final long userId, final long postId, final String content) {

        Post post = findPostByIdForUpdate(postId);

        Comment comment = Comment.withoutId(content, userId, postId, null);
        commentRepository.save(comment);

        postRepository.save(post.increaseCommentCount());

        // notificationService.createCommentNotification(post, commentJpaEntity);
        // 알람 서비스 로직 수정 후 주석 해제
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

        postRepository.save(post.increaseCommentCount());

        // notificationService.createReplyNotification(post, commentJpaEntity);
    }

    @Override
    public void removeCommentFromPost(final long userId, final long postId, final long commentId) {
        Post post = findPostByIdForUpdate(postId);
        Comment comment = findCommentByIdForUpdate(commentId);

        comment.validateDeletableBy(userId, post.getId());

        postRepository.save(post.decreaseCommentCount());

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
