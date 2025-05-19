package com.ripple.BE.post.adapter.out.persistence.jpa.repository.postlike;

import com.ripple.BE.post.adapter.out.persistence.jpa.entity.PostJpaEntity;
import java.util.List;

public interface PostLikeQueryRepository {

    List<PostJpaEntity> findPostsLikedByUser(Long userId);
}
