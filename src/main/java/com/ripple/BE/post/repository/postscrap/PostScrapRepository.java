package com.ripple.BE.post.repository.postscrap;

import com.ripple.BE.post.domain.PostScrap;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostScrapRepository
        extends JpaRepository<PostScrap, Long>, PostScrapRepositoryCustom {

    Optional<PostScrap> findByPostIdAndUserId(long postId, long userId);

    boolean existsByPostIdAndUserId(long postId, long userId);
}
