package com.ripple.BE.post.persistence;

import com.ripple.BE.post.domain.comment.CommentLike;
import com.ripple.BE.post.persistence.dto.LikeCommentWithPostDTO;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CommentLikeRepository {

    Optional<CommentLike> findByCommentIdAndUserId(final long commentId, final long userId);

    boolean existsByCommentIdAndUserId(final long commentId, final long userId);

    void save(final CommentLike commentLike);

    void delete(final CommentLike commentLike);

    void deleteAllByCommentId(final long commentId);

    void deleteAllByPostId(final long postId);

    List<CommentLike> findByUserIdAndCommentIdIn(final long userId, final Set<Long> commentIds);

    List<LikeCommentWithPostDTO> findLikedCommentsByUserIdWithPost(long userId);

    void deleteAllByCommentIdIn(final List<Long> commentIds);

    void deleteAllByUserId(final long userId);
}
