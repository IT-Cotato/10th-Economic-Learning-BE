package com.ripple.BE.post.adapter.out.persistence.jpa.repository.commentlike;

import static com.ripple.BE.post.adapter.out.persistence.jpa.entity.QCommentJpaEntity.*;
import static com.ripple.BE.post.adapter.out.persistence.jpa.entity.QCommentLikeJpaEntity.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ripple.BE.post.adapter.out.persistence.jpa.entity.CommentJpaEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentLikeQueryRepositoryImpl implements CommentLikeQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommentJpaEntity> findCommentsLikedByUser(Long userId) {

        return queryFactory
                .select(commentJpaEntity)
                .from(commentLikeJpaEntity)
                .join(commentLikeJpaEntity.comment, commentJpaEntity)
                .where(commentLikeJpaEntity.user.id.eq(userId))
                .fetch();
    }
}
