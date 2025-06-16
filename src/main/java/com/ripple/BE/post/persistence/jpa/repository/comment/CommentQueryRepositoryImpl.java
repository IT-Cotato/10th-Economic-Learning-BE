package com.ripple.BE.post.persistence.jpa.repository.comment;

import static com.ripple.BE.post.persistence.jpa.entity.QCommentJpaEntity.*;
import static com.ripple.BE.post.persistence.jpa.entity.QPostJpaEntity.*;
import static com.ripple.BE.user.domain.QUser.*;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ripple.BE.post.persistence.dto.CommentWithPostDTO;
import com.ripple.BE.post.persistence.dto.CommentWithUserDTO;
import com.ripple.BE.user.domain.User;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentQueryRepositoryImpl implements CommentQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<User> findUsersByToktokPostId(long toktokPostId) {

        return queryFactory
                .select(user)
                .from(commentJpaEntity)
                .join(user)
                .on(commentJpaEntity.commenterId.eq(user.id))
                .where(commentJpaEntity.postId.eq(toktokPostId))
                .distinct()
                .fetch();
    }

    @Override
    public Map<Long, List<User>> findUsersByToktokPostIds(List<Long> postIds) {
        return queryFactory
                .select(commentJpaEntity.postId, user)
                .from(commentJpaEntity)
                .join(user)
                .on(commentJpaEntity.commenterId.eq(user.id))
                .where(commentJpaEntity.postId.in(postIds))
                .distinct()
                .fetch()
                .stream()
                .filter(tuple -> tuple.get(commentJpaEntity.postId) != null && tuple.get(user) != null)
                .collect(
                        Collectors.groupingBy(
                                tuple -> tuple.get(commentJpaEntity.postId),
                                Collectors.mapping(tuple -> tuple.get(user), Collectors.toList())));
    }

    @Override
    public List<CommentWithUserDTO> findAllByPostIdWithUser(long postId) {
        List<CommentWithUserDTO> dtos =
                queryFactory
                        .select(
                                Projections.constructor(
                                        CommentWithUserDTO.class,
                                        commentJpaEntity.id,
                                        commentJpaEntity.content,
                                        commentJpaEntity.likeCount,
                                        commentJpaEntity.replyCount,
                                        commentJpaEntity.parentId,
                                        commentJpaEntity.isDeleted,
                                        user.id,
                                        user.nickname,
                                        user.profileImage,
                                        commentJpaEntity.createdDate))
                        .from(commentJpaEntity)
                        .leftJoin(user)
                        .on(commentJpaEntity.commenterId.eq(user.id))
                        .leftJoin(user.profileImage)
                        .where(commentJpaEntity.postId.eq(postId))
                        .fetch();

        return dtos;
    }

    @Override
    public List<CommentWithPostDTO> findUserCommentsWithPost(final long userId) {

        return queryFactory
                .select(
                        Projections.constructor(
                                CommentWithPostDTO.class,
                                commentJpaEntity.id,
                                commentJpaEntity.content,
                                postJpaEntity.title,
                                postJpaEntity.type,
                                commentJpaEntity.createdDate))
                .from(commentJpaEntity)
                .join(postJpaEntity)
                .on(commentJpaEntity.postId.eq(postJpaEntity.id))
                .where(commentJpaEntity.commenterId.eq(userId))
                .orderBy(commentJpaEntity.createdDate.desc())
                .fetch();
    }
}
