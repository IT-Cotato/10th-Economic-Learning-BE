package com.ripple.BE.post.persistence.jpa.repository.post;

import com.ripple.BE.post.domain.type.PostSort;
import com.ripple.BE.post.domain.type.PostType;
import com.ripple.BE.post.persistence.jpa.entity.PostJpaEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostQueryRepository {

    Page<PostJpaEntity> findByType(PostType type, PostSort postSort, Pageable pageable);

    Page<PostJpaEntity> findNormalPosts(Pageable pageable, PostSort postSort);

    List<PostJpaEntity> findPopularPosts();

    List<PostJpaEntity> findUserNormalPosts(long userId);

    Page<PostJpaEntity> searchNormalPosts(String keyword, Pageable pageable);
}
