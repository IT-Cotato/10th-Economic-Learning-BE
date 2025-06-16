package com.ripple.BE.post.persistence.jpa.repository.post;

import com.ripple.BE.post.domain.type.PostSort;
import com.ripple.BE.post.domain.type.PostType;
import com.ripple.BE.post.persistence.dto.PostWithScrapAndImageDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostQueryRepository {

    Page<PostWithScrapAndImageDTO> findByType(
            PostType type, PostSort postSort, Pageable pageable, long userId);

    Page<PostWithScrapAndImageDTO> findNormalPosts(Pageable pageable, PostSort postSort, long userId);

    Page<PostWithScrapAndImageDTO> searchNormalPosts(String keyword, Pageable pageable, long userId);

    List<PostWithScrapAndImageDTO> findPopularPosts(long userId);

    List<PostWithScrapAndImageDTO> findUserNormalPosts(long userId);

    List<PostWithScrapAndImageDTO> findByIdIn(List<Long> ids, long userId);
}
