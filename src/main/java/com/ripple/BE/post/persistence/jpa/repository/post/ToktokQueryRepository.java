package com.ripple.BE.post.persistence.jpa.repository.post;

import com.ripple.BE.post.domain.type.PostSort;
import com.ripple.BE.post.persistence.dto.ToktokWithScrapAndImageDTO;
import com.ripple.BE.post.persistence.jpa.entity.PostJpaEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ToktokQueryRepository {

    Set<String> findAllTitles();

    List<PostJpaEntity> findNewToktokPosts();

    Page<ToktokWithScrapAndImageDTO> findUsedToktokPosts(
            Pageable pageable, PostSort postSort, long userId);

    Optional<ToktokWithScrapAndImageDTO> findByUsedDate(LocalDate usedDate, long userId);

    Page<ToktokWithScrapAndImageDTO> searchUsedToktokPosts(
            String keyword, Pageable pageable, long userId);
}
