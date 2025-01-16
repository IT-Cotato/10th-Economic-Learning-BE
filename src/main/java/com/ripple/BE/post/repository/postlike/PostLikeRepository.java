package com.ripple.BE.post.repository.postlike;

import com.ripple.BE.post.domain.PostLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository
        extends JpaRepository<PostLike, Long>, PostLikeRepositoryCustom {

    Optional<PostLike> findByPostIdAndUserId(long postId, long userId);

    boolean existsByPostIdAndUserId(long postId, long userId);
}
