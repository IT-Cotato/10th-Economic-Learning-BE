package com.ripple.BE.post.persistence.jpa.repository.comment;

import static com.ripple.BE.post.persistence.jpa.entity.QCommentJpaEntity.*;
import static com.ripple.BE.user.domain.QUser.*;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ripple.BE.post.persistence.dto.CommentWithUserDTO;
import com.ripple.BE.user.domain.User;
import java.util.List;
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

        System.out.println("CommentWithUserDTOs: " + dtos);
        return dtos;
    }
}
