package com.ripple.BE.post.repository.comment;

import com.ripple.BE.post.domain.Comment;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Comment> findByIdAndCommenterId(Long commentId, Long commenterId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Comment c WHERE c.id = :commentId")
    Optional<Comment> findByIdForUpdate(Long commentId);
}
