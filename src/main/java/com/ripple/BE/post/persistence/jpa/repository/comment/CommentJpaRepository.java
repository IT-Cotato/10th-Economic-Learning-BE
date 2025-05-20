package com.ripple.BE.post.persistence.jpa.repository.comment;

import com.ripple.BE.post.persistence.jpa.entity.CommentJpaEntity;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface CommentJpaRepository
        extends JpaRepository<CommentJpaEntity, Long>, CommentQueryRepository {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM CommentJpaEntity c WHERE c.id = :commentId")
    Optional<CommentJpaEntity> findByIdForUpdate(Long commentId);

    List<CommentJpaEntity> findAllByCommenterId(Long userId);

    List<CommentJpaEntity> findChildrenByParentId(Long parentId);

    void deleteAllByPostId(Long postId);
}
