package com.ripple.BE.post.persistence.jpa.repository.post;

import static com.ripple.BE.post.persistence.jpa.entity.QPostJpaEntity.*;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ripple.BE.post.domain.type.PostSort;
import com.ripple.BE.post.domain.type.PostType;
import com.ripple.BE.post.persistence.jpa.entity.PostJpaEntity;
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
    public Page<PostJpaEntity> findByType(PostType type, PostSort postSort, Pageable pageable) {
        BooleanExpression predicate = postJpaEntity.type.eq(type);

        List<PostJpaEntity> posts = getPostsByPageable(pageable, predicate, postSort);
        JPAQuery<Long> countQuery =
                queryFactory.select(postJpaEntity.count()).from(postJpaEntity).where(predicate);

        return PageableExecutionUtils.getPage(posts, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<PostJpaEntity> findNormalPosts(Pageable pageable, PostSort postSort) {

        BooleanExpression predicate = postJpaEntity.type.ne(PostType.ECONOMY_TALK);
        List<PostJpaEntity> posts = getPostsByPageable(pageable, predicate, postSort);

        JPAQuery<Long> countQuery =
                queryFactory.select(postJpaEntity.count()).from(postJpaEntity).where(predicate);

        return PageableExecutionUtils.getPage(posts, pageable, countQuery::fetchOne);
    }

    @Override
    public List<PostJpaEntity> findPopularPosts() {
        return queryFactory
                .selectFrom(postJpaEntity)
                .where(postJpaEntity.likeCount.goe(POPULAR_POST_LIKE_COUNT))
                .orderBy(postJpaEntity.createdDate.desc())
                .limit(POPULAR_POST_LIMIT)
                .fetch();
    }

    @Override
    public List<PostJpaEntity> findUserNormalPosts(long userId) {
        BooleanExpression predicate =
                postJpaEntity.author.id.eq(userId).and(postJpaEntity.type.ne(PostType.ECONOMY_TALK));

        return queryFactory
                .selectFrom(postJpaEntity)
                .where(predicate)
                .orderBy(postJpaEntity.createdDate.desc())
                .fetch();
    }

    @Override
    public Page<PostJpaEntity> searchNormalPosts(String keyword, Pageable pageable) {
        BooleanExpression predicate = postJpaEntity.type.ne(PostType.ECONOMY_TALK);

        if (keyword != null && !keyword.trim().isEmpty()) {
            predicate = postJpaEntity.title.contains(keyword).or(postJpaEntity.content.contains(keyword));
        }

        List<PostJpaEntity> posts = getPostsByPageable(pageable, predicate, PostSort.RECENT);

        JPAQuery<Long> countQuery =
                queryFactory.select(postJpaEntity.count()).from(postJpaEntity).where(predicate);

        return PageableExecutionUtils.getPage(posts, pageable, countQuery::fetchOne);
    }

    private List<PostJpaEntity> getPostsByPageable(
            Pageable pageable, BooleanExpression predicate, PostSort postSort) {
        var orderBy =
                (postSort == PostSort.POPULAR)
                        ? new OrderSpecifier[] {
                            postJpaEntity.likeCount.desc(), postJpaEntity.createdDate.desc()
                        }
                        : new OrderSpecifier[] {postJpaEntity.createdDate.desc()};

        return queryFactory
                .selectFrom(postJpaEntity)
                .where(predicate)
                .orderBy(orderBy)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
