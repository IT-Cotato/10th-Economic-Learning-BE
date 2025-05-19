package com.ripple.BE.post.persistence.jpa.repository.postlike;

import static com.ripple.BE.post.persistence.jpa.entity.QPostJpaEntity.*;
import static com.ripple.BE.post.persistence.jpa.entity.QPostLikeJpaEntity.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ripple.BE.post.persistence.jpa.entity.PostJpaEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostLikeQueryRepositoryImpl implements PostLikeQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PostJpaEntity> findPostsLikedByUser(long userId) {

        return queryFactory
                .select(postJpaEntity)
                .from(postLikeJpaEntity)
                .join(postLikeJpaEntity.post, postJpaEntity)
                .where(postLikeJpaEntity.user.id.eq(userId))
                .fetch();
    }
}
