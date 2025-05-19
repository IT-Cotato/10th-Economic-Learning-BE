package com.ripple.BE.post.persistence.jpa.repository.postlike;

import com.ripple.BE.post.persistence.jpa.entity.PostJpaEntity;
import java.util.List;

public interface PostLikeQueryRepository {

    List<PostJpaEntity> findPostsLikedByUser(long userId);
}
