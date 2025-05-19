package com.ripple.BE.post.persistence.jpa.repository.commentlike;

import com.ripple.BE.post.persistence.jpa.entity.CommentJpaEntity;
import java.util.List;

public interface CommentLikeQueryRepository {
    List<CommentJpaEntity> findCommentsLikedByUser(long userId);
}
