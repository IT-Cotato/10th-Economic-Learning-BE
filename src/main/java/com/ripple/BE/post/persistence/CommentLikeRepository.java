package com.ripple.BE.post.persistence;

import com.ripple.BE.post.domain.comment.Comment;
import com.ripple.BE.post.domain.comment.CommentLike;
import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository {

    Optional<CommentLike> findByCommentIdAndUserId(final long commentId, final long userId);

    boolean existsByCommentIdAndUserId(final long commentId, final long userId);

    // 댓글 좋아요를 누른 유저의 댓글 목록을 가져온다.
    List<Comment> findCommentsLikedByUser(final long userId);

    void save(final CommentLike commentLike);

    void delete(final CommentLike commentLike);
}
