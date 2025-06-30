package com.ripple.BE.post.persistence.jpa.repository.post;

import com.ripple.BE.post.domain.type.PostSort;
import com.ripple.BE.post.domain.type.PostType;
import com.ripple.BE.post.persistence.dto.PostDetailDTO;
import com.ripple.BE.post.persistence.dto.PostWithImageDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostQueryRepository {

    Page<PostWithImageDTO> findByType(PostType type, PostSort postSort, Pageable pageable);

    Page<PostWithImageDTO> findNormalPosts(Pageable pageable, PostSort postSort);

    Page<PostWithImageDTO> searchNormalPosts(String keyword, Pageable pageable);

    List<PostWithImageDTO> findPopularPosts();

    List<PostWithImageDTO> findUserNormalPosts(long userId);

    List<PostWithImageDTO> findByIdIn(List<Long> ids);

    Optional<PostDetailDTO> findPostDetail(long postId, long userId);
}
