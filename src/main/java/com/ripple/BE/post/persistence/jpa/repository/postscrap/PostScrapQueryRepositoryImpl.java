package com.ripple.BE.post.persistence.jpa.repository.postscrap;

import static com.ripple.BE.image.persistence.jpa.entity.QImageJpaEntity.*;
import static com.ripple.BE.post.persistence.jpa.entity.QPostJpaEntity.*;
import static com.ripple.BE.post.persistence.jpa.entity.QPostScrapJpaEntity.*;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ripple.BE.post.persistence.dto.PostWithScrapAndImageDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostScrapQueryRepositoryImpl implements PostScrapQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PostWithScrapAndImageDTO> findPostsScrappedByUser(long userId) {
        return queryFactory
                .select(
                        Projections.constructor(
                                PostWithScrapAndImageDTO.class,
                                postJpaEntity.id,
                                postJpaEntity.title,
                                postJpaEntity.content,
                                postJpaEntity.type,
                                postJpaEntity.likeCount,
                                postJpaEntity.commentCount,
                                // 대표 이미지 URL
                                JPAExpressions.select(imageJpaEntity.s3Info.url)
                                        .from(imageJpaEntity)
                                        .where(imageJpaEntity.postId.eq(postJpaEntity.id))
                                        .orderBy(imageJpaEntity.id.asc())
                                        .limit(1),
                                // 스크랩 여부는 항상 true
                                Expressions.constant(true),
                                postJpaEntity.createdDate))
                .from(postScrapJpaEntity)
                .join(postJpaEntity)
                .on(postScrapJpaEntity.postId.eq(postJpaEntity.id))
                .where(postScrapJpaEntity.userId.eq(userId))
                .orderBy(postScrapJpaEntity.createdDate.desc())
                .fetch();
    }
}
