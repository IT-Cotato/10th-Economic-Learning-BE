package com.ripple.BE.post.persistence.jpa.repository.commentlike;

import com.ripple.BE.post.persistence.jpa.entity.CommentLikeJpaEntity;

import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommentLikeJpaRepository
	extends JpaRepository<CommentLikeJpaEntity, Long>, CommentLikeQueryRepository {

	Optional<CommentLikeJpaEntity> findByCommentIdAndUserId(long commentId, long userId);

	boolean existsByCommentIdAndUserId(long commentId, long userId);

	void deleteAllByCommentId(long commentId);

	@Modifying
	@Query("""
		    DELETE FROM CommentLikeJpaEntity cl
		    WHERE cl.commentId IN (
		        SELECT c.id FROM CommentJpaEntity c WHERE c.postId = :postId
		    )
		""")
	void deleteAllByPostId(@Param("postId") Long postId);

	List<CommentLikeJpaEntity> findByUserIdAndCommentIdIn(long userId, Set<Long> commentIds);
}
