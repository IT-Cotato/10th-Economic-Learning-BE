package com.ripple.BE.post.persistence.jpa.repository.commentlike;

import com.ripple.BE.post.persistence.jpa.entity.CommentLikeJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeJpaRepository
        extends JpaRepository<CommentLikeJpaEntity, Long>, CommentLikeQueryRepository {

    Optional<CommentLikeJpaEntity> findByCommentIdAndUserId(long commentId, long userId);

    boolean existsByCommentIdAndUserId(long commentId, long userId);

    void deleteAllByCommentId(long commentId);
}
