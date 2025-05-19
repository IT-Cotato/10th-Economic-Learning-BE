package com.ripple.BE.post.adapter.out.persistence.jpa.repository.postscrap;

import com.ripple.BE.post.adapter.out.persistence.jpa.entity.PostJpaEntity;
import java.util.List;

public interface PostScrapQueryRepository {

    List<PostJpaEntity> findPostsScrappedByUser(Long userId);
}
