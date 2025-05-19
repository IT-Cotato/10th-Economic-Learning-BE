package com.ripple.BE.post.persistence.jpa.repository.comment;

import static com.ripple.BE.post.persistence.jpa.entity.QCommentJpaEntity.*;
import static com.ripple.BE.post.persistence.jpa.entity.QPostJpaEntity.*;
import static com.ripple.BE.user.domain.QUser.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ripple.BE.post.persistence.jpa.entity.CommentJpaEntity;
import com.ripple.BE.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentQueryRepositoryImpl implements CommentQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommentJpaEntity> findRootCommentsByPost(long postId) {

        return queryFactory
                .selectFrom(commentJpaEntity)
                .where(commentJpaEntity.post.id.eq(postId).and(commentJpaEntity.parent.isNull()))
                .orderBy(commentJpaEntity.createdDate.asc())
                .fetch();
    }

    @Override
    public List<User> findUsersByToktokPostId(long toktokPostId) {

        return queryFactory
                .select(user)
                .from(commentJpaEntity)
                .join(commentJpaEntity.commenter, user)
                .where(commentJpaEntity.post.id.eq(toktokPostId))
                .distinct()
                .fetch();
    }
}
