package com.ripple.BE.post.repository;

import com.ripple.BE.post.domain.Comment;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByIdAndCommenterId(Long commentId, Long commenterId);

    List<Comment> findAllByPostId(Long postId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Comment c WHERE c.id = :commentId")
    Optional<Comment> findByIdForUpdate(Long commentId);

    @Query(
            "SELECT c FROM Comment c WHERE c.post.id = :postId AND c.parent IS NULL ORDER BY c.createdDate ASC")
    List<Comment> findRootCommentsByPostId(@Param("postId") long postId);
}
