package com.ripple.BE.post.persistence.jpa.repository.postscrap;

import com.ripple.BE.post.persistence.jpa.entity.PostJpaEntity;
import java.util.List;

public interface PostScrapQueryRepository {

    List<PostJpaEntity> findPostsScrappedByUser(long userId);
}
