package com.ripple.BE.post.persistence.jpa.repository.commentlike;

import static com.ripple.BE.post.persistence.jpa.entity.QCommentJpaEntity.*;
import static com.ripple.BE.post.persistence.jpa.entity.QCommentLikeJpaEntity.*;
import static com.ripple.BE.post.persistence.jpa.entity.QPostJpaEntity.*;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ripple.BE.post.persistence.dto.LikeCommentWithPostDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentLikeQueryRepositoryImpl implements CommentLikeQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<LikeCommentWithPostDTO> findLikedCommentsByUserIdWithPost(long userId) {
        return queryFactory
                .select(
                        Projections.constructor(
                                LikeCommentWithPostDTO.class,
                                commentJpaEntity.id,
                                commentJpaEntity.content,
                                commentJpaEntity.createdDate,
                                postJpaEntity.id,
                                postJpaEntity.title,
                                postJpaEntity.type))
                .from(commentLikeJpaEntity)
                .join(commentJpaEntity)
                .on(commentLikeJpaEntity.commentId.eq(commentJpaEntity.id))
                .join(postJpaEntity)
                .on(commentJpaEntity.postId.eq(postJpaEntity.id))
                .where(commentLikeJpaEntity.userId.eq(userId), commentJpaEntity.isDeleted.isFalse())
                .orderBy(commentLikeJpaEntity.createdDate.desc())
                .fetch();
    }
}
