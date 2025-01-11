package com.ripple.BE.post.repository.commentlike;

import com.ripple.BE.post.domain.CommentLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    Optional<CommentLike> findByCommentIdAndUserId(long commentId, long userId);

    boolean existsByCommentIdAndUserId(long commentId, long userId);
}
