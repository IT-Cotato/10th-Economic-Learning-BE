package com.ripple.BE.post.persistence.jpa.repository.postscrap;

import com.ripple.BE.post.persistence.jpa.entity.PostScrapJpaEntity;
import java.util.Collection;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostScrapJpaRepository
        extends JpaRepository<PostScrapJpaEntity, Long>, PostScrapQueryRepository {

    Optional<PostScrapJpaEntity> findByPostIdAndUserId(long postId, long userId);

    boolean existsByPostIdAndUserId(long postId, long userId);

    void deleteAllByPostId(long postId);

    void deleteAllByPostIdIn(Collection<Long> postId);
}
