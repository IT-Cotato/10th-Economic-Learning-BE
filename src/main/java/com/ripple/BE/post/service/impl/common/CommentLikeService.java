package com.ripple.BE.post.service.impl.common;

import static com.ripple.BE.post.exception.errorcode.PostErrorCode.*;

import com.ripple.BE.post.domain.comment.Comment;
import com.ripple.BE.post.domain.comment.CommentLike;
import com.ripple.BE.post.domain.post.Post;
import com.ripple.BE.post.exception.PostException;
import com.ripple.BE.post.persistence.CommentLikeRepository;
import com.ripple.BE.post.persistence.CommentRepository;
import com.ripple.BE.post.persistence.PostRepository;
import com.ripple.BE.post.service.CommentLikeUseCase;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.service.UserService;
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

    private final UserService userService;

    @Override
    public void addLikeToComment(final long commentId, final long userId, final long postId) {

        Post post =
                postRepository.findById(postId).orElseThrow(() -> new PostException(POST_NOT_FOUND));
        Comment comment = findCommentByIdForUpdate(commentId);
        User user = userService.findUserById(userId);

        if (commentLikeRepository.existsByCommentIdAndUserId(commentId, userId)) {
            throw new PostException(LIKE_ALREADY_EXISTS);
        }
        comment.validateUpdatableBy(user, post);

        CommentLike commentLike = CommentLike.of(user, comment);
        commentLikeRepository.save(commentLike);

        comment.increaseLikeCount();
        commentRepository.updateLikeCount(comment);
    }

    @Override
    public void removeLikeFromComment(final long commentId, final long userId, final long postId) {

        Post post =
                postRepository.findById(postId).orElseThrow(() -> new PostException(POST_NOT_FOUND));
        Comment comment = findCommentByIdForUpdate(commentId);
        User user = userService.findUserById(userId);

        CommentLike commentLike =
                commentLikeRepository
                        .findByCommentIdAndUserId(commentId, userId)
                        .orElseThrow(() -> new PostException(LIKE_NOT_FOUND));

        comment.validateUpdatableBy(user, post);
        commentLikeRepository.delete(commentLike);

        comment.decreaseLikeCount();
        commentRepository.updateLikeCount(comment);
    }

    private Comment findCommentByIdForUpdate(final long id) {
        return commentRepository
                .findByIdForUpdate(id)
                .orElseThrow(() -> new PostException(COMMENT_NOT_FOUND));
    }
}
