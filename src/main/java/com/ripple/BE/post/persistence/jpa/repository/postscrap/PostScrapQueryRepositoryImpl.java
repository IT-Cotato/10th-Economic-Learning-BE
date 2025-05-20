package com.ripple.BE.post.persistence.jpa.repository.postscrap;

import static com.ripple.BE.post.persistence.jpa.entity.QPostJpaEntity.*;
import static com.ripple.BE.post.persistence.jpa.entity.QPostScrapJpaEntity.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ripple.BE.post.persistence.jpa.entity.PostJpaEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostScrapQueryRepositoryImpl implements PostScrapQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PostJpaEntity> findPostsScrappedByUser(long userId) {
        return queryFactory
                .select(postJpaEntity)
                .from(postScrapJpaEntity)
                .join(postScrapJpaEntity.post, postJpaEntity)
                .where(postScrapJpaEntity.user.id.eq(userId))
                .orderBy(postScrapJpaEntity.createdDate.desc())
                .fetch();
    }
}
