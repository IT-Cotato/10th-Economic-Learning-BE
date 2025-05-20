package com.ripple.BE.post.persistence.jpa.repository.post;

import static com.ripple.BE.post.persistence.jpa.entity.QPostJpaEntity.*;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ripple.BE.post.domain.type.PostSort;
import com.ripple.BE.post.domain.type.PostType;
import com.ripple.BE.post.persistence.jpa.entity.PostJpaEntity;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class ToktokQueryRepositoryImpl implements ToktokQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Set<String> findAllTitles() {
        BooleanExpression predicate =
                postJpaEntity.type.eq(PostType.ECONOMY_TALK).and(postJpaEntity.usedDate.isNotNull());

        return new HashSet<>(
                jpaQueryFactory.select(postJpaEntity.title).from(postJpaEntity).where(predicate).fetch());
    }

    @Override
    public Page<PostJpaEntity> searchUsedToktokPosts(String keyword, Pageable pageable) {

        BooleanExpression predicate =
                postJpaEntity.type.eq(PostType.ECONOMY_TALK).and(postJpaEntity.usedDate.isNotNull());

        if (keyword != null && !keyword.trim().isEmpty()) {
            predicate =
                    predicate.and(
                            postJpaEntity.title.contains(keyword).or(postJpaEntity.content.contains(keyword)));
        }

        List<PostJpaEntity> posts = getPostsByPageable(pageable, predicate, PostSort.RECENT);

        JPAQuery<Long> countQuery =
                jpaQueryFactory.select(postJpaEntity.count()).from(postJpaEntity).where(predicate);

        return PageableExecutionUtils.getPage(posts, pageable, countQuery::fetchOne);
    }

    @Override
    public List<PostJpaEntity> findNewToktokPosts() {
        BooleanExpression predicate =
                postJpaEntity.type.eq(PostType.ECONOMY_TALK).and(postJpaEntity.usedDate.isNull());

        return jpaQueryFactory.selectFrom(postJpaEntity).where(predicate).fetch();
    }

    @Override
    public Page<PostJpaEntity> findUsedToktokPosts(Pageable pageable, PostSort postSort) {
        BooleanExpression predicate =
                postJpaEntity.type.eq(PostType.ECONOMY_TALK).and(postJpaEntity.usedDate.isNotNull());

        List<PostJpaEntity> posts = getPostsByPageable(pageable, predicate, postSort);

        JPAQuery<Long> countQuery =
                jpaQueryFactory.select(postJpaEntity.count()).from(postJpaEntity).where(predicate);

        return PageableExecutionUtils.getPage(posts, pageable, countQuery::fetchOne);
    }

    @Override
    public Optional<PostJpaEntity> findByUsedDate(LocalDate usedDate) {
        BooleanExpression predicate =
                postJpaEntity.type.eq(PostType.ECONOMY_TALK).and(postJpaEntity.usedDate.eq(usedDate));

        PostJpaEntity result = jpaQueryFactory.selectFrom(postJpaEntity).where(predicate).fetchOne();

        return Optional.ofNullable(result);
    }

    private List<PostJpaEntity> getPostsByPageable(
            Pageable pageable, BooleanExpression predicate, PostSort postSort) {
        OrderSpecifier<?>[] orderBy =
                (postSort == PostSort.POPULAR)
                        ? new OrderSpecifier[] {
                            postJpaEntity.likeCount.desc(), postJpaEntity.createdDate.desc()
                        }
                        : new OrderSpecifier[] {
                            postJpaEntity.usedDate != null
                                    ? postJpaEntity.usedDate.desc()
                                    : postJpaEntity.createdDate.desc()
                        };

        return jpaQueryFactory
                .selectFrom(postJpaEntity)
                .where(predicate)
                .orderBy(orderBy)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
