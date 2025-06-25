package com.ripple.BE.post.persistence.jpa.repository.post;

import static com.ripple.BE.image.persistence.jpa.entity.QImageJpaEntity.*;
import static com.ripple.BE.post.persistence.jpa.entity.QPostJpaEntity.*;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ripple.BE.post.domain.type.PostSort;
import com.ripple.BE.post.domain.type.PostType;
import com.ripple.BE.post.persistence.dto.PostWithImageDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class PostQueryRepositoryImpl implements PostQueryRepository {

    private final JPAQueryFactory queryFactory;
    private static final int POPULAR_POST_LIKE_COUNT = 10;
    private static final int POPULAR_POST_LIMIT = 10;

    @Override
    public Page<PostWithImageDTO> findByType(PostType type, PostSort postSort, Pageable pageable) {
        BooleanExpression predicate = postJpaEntity.type.eq(type);
        return getPostDTOsByPageable(predicate, pageable, postSort);
    }

    @Override
    public Page<PostWithImageDTO> findNormalPosts(Pageable pageable, PostSort postSort) {
        BooleanExpression predicate = postJpaEntity.type.ne(PostType.ECONOMY_TALK);
        return getPostDTOsByPageable(predicate, pageable, postSort);
    }

    @Override
    public Page<PostWithImageDTO> searchNormalPosts(String keyword, Pageable pageable) {
        BooleanExpression predicate = postJpaEntity.type.ne(PostType.ECONOMY_TALK);

        if (keyword != null && !keyword.trim().isEmpty()) {
            predicate =
                    predicate.and(
                            postJpaEntity.title.contains(keyword).or(postJpaEntity.content.contains(keyword)));
        }

        return getPostDTOsByPageable(predicate, pageable, PostSort.RECENT);
    }

    @Override
    public List<PostWithImageDTO> findPopularPosts() {
        BooleanExpression predicate = postJpaEntity.likeCount.goe(POPULAR_POST_LIKE_COUNT);

        return queryFactory
                .select(selectPostDTOProjection())
                .from(postJpaEntity)
                .where(predicate)
                .orderBy(postJpaEntity.createdDate.desc())
                .limit(POPULAR_POST_LIMIT)
                .fetch();
    }

    @Override
    public List<PostWithImageDTO> findUserNormalPosts(long userId) {
        BooleanExpression predicate =
                postJpaEntity.authorId.eq(userId).and(postJpaEntity.type.ne(PostType.ECONOMY_TALK));

        return queryFactory
                .select(selectPostDTOProjection())
                .from(postJpaEntity)
                .where(predicate)
                .orderBy(postJpaEntity.createdDate.desc())
                .fetch();
    }

    // 공통 페이징 로직
    private Page<PostWithImageDTO> getPostDTOsByPageable(
            BooleanExpression predicate, Pageable pageable, PostSort postSort) {

        List<PostWithImageDTO> content =
                queryFactory
                        .select(selectPostDTOProjection())
                        .from(postJpaEntity)
                        .where(predicate)
                        .orderBy(getOrderSpecifiers(postSort))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        JPAQuery<Long> countQuery =
                queryFactory.select(postJpaEntity.count()).from(postJpaEntity).where(predicate);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public List<PostWithImageDTO> findByIdIn(List<Long> ids) {
        BooleanExpression predicate = postJpaEntity.id.in(ids);

        return queryFactory
                .select(selectPostDTOProjection())
                .from(postJpaEntity)
                .where(predicate)
                .orderBy(postJpaEntity.createdDate.desc())
                .fetch();
    }

    // 공통 정렬 조건
    private OrderSpecifier<?>[] getOrderSpecifiers(PostSort sort) {
        return (sort == PostSort.POPULAR)
                ? new OrderSpecifier[] {postJpaEntity.likeCount.desc(), postJpaEntity.createdDate.desc()}
                : new OrderSpecifier[] {postJpaEntity.createdDate.desc()};
    }

    // 공통 Projection
    private ConstructorExpression<PostWithImageDTO> selectPostDTOProjection() {
        return Projections.constructor(
                PostWithImageDTO.class,
                postJpaEntity.id,
                postJpaEntity.title,
                postJpaEntity.content,
                postJpaEntity.type,
                postJpaEntity.likeCount,
                postJpaEntity.commentCount,
                JPAExpressions.select(imageJpaEntity.s3Info.url)
                        .from(imageJpaEntity)
                        .where(imageJpaEntity.postId.eq(postJpaEntity.id))
                        .orderBy(imageJpaEntity.id.asc())
                        .limit(1),
                postJpaEntity.createdDate);
    }
}
