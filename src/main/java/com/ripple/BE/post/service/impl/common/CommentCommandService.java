package com.ripple.BE.post.service.impl.common;

import static com.ripple.BE.post.exception.errorcode.PostErrorCode.*;

import com.ripple.BE.notification.service.NotificationService;
import com.ripple.BE.post.domain.comment.Comment;
import com.ripple.BE.post.domain.post.Post;
import com.ripple.BE.post.exception.PostException;
import com.ripple.BE.post.persistence.CommentRepository;
import com.ripple.BE.post.persistence.PostRepository;
import com.ripple.BE.post.service.CommentCommandUseCase;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentCommandService implements CommentCommandUseCase {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    private final UserService userService;
    private final NotificationService notificationService;

    @Override
    public void addCommentToPost(final long userId, final long postId, final String content) {

        Post post = findPostByIdForUpdate(postId);
        User user = userService.findUserById(userId);

        Comment comment = Comment.of(content, user, post);
        commentRepository.save(comment);

        post.increaseCommentCount();
        postRepository.updateCommentCount(post);

        // notificationService.createCommentNotification(post, commentJpaEntity);
        // 알람 서비스 로직 수정 후 주석 해제
    }

    @Override
    public void addReplyToComment(
            final long userId, final long postId, final long commentId, final String content) {

        Post post = findPostByIdForUpdate(postId);
        User user = userService.findUserById(userId);
        Comment parentComment = findCommentByIdForUpdate(commentId);

        parentComment.validateReplyable(post);

        Comment comment = Comment.of(content, user, post, parentComment);
        commentRepository.save(comment);

        parentComment.increaseReplyCount();
        commentRepository.updateReplyCount(parentComment);

        post.increaseCommentCount();
        postRepository.updateCommentCount(post);

        // notificationService.createReplyNotification(post, commentJpaEntity);
    }

    @Override
    public void removeCommentFromPost(final long userId, final long postId, final long commentId) {
        Post post = findPostByIdForUpdate(postId);
        Comment comment = findCommentByIdForUpdate(commentId);
        User user = userService.findUserById(userId);

        comment.validateDeletableBy(user, post);

        post.decreaseCommentCount();
        postRepository.updateCommentCount(post);

        if (comment.isRoot()) {
            handleRootComment(comment);
        } else {
            handleChildComment(comment, comment.getParent());
        }
    }

    private void handleRootComment(Comment comment) {
        if (comment.hasNoChildren()) {
            commentRepository.delete(comment);
        } else {
            comment.softDeleteAsRoot();
        }
    }

    private void handleChildComment(Comment comment, Comment parent) {
        commentRepository.delete(comment);

        parent.decreaseReplyCount();
        commentRepository.updateReplyCount(parent);

        if (parent.isDeleted() && parent.hasNoChildren()) {
            commentRepository.delete(parent);
        }
    }

    @Override
    public void updateComment(
            final long userId, final long postId, final long commentId, final String content) {
        Post post = findPostByIdForUpdate(postId);
        Comment comment = findCommentByIdForUpdate(commentId);
        User user = userService.findUserById(userId);

        comment.validateUpdatableBy(user, post);
        comment.updateContent(content);

        commentRepository.save(comment);
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
