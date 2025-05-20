package com.ripple.BE.post.persistence.jpa.repository.postlike;

import com.ripple.BE.post.persistence.jpa.entity.PostLikeJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeJpaRepository
        extends JpaRepository<PostLikeJpaEntity, Long>, PostLikeQueryRepository {

    Optional<PostLikeJpaEntity> findByPostIdAndUserId(long postId, long userId);

    boolean existsByPostIdAndUserId(long postId, long userId);

    void deleteAllByPostId(long postId);
}
