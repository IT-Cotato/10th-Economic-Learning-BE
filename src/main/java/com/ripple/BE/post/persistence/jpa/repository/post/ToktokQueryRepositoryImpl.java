package com.ripple.BE.post.persistence.jpa.repository.post;

import static com.ripple.BE.image.persistence.jpa.entity.QImageJpaEntity.*;
import static com.ripple.BE.post.persistence.jpa.entity.QPostJpaEntity.*;
import static com.ripple.BE.post.persistence.jpa.entity.QPostScrapJpaEntity.*;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ripple.BE.post.domain.type.PostSort;
import com.ripple.BE.post.domain.type.PostType;
import com.ripple.BE.post.persistence.dto.ToktokWithScrapAndImageDTO;
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
        BooleanExpression predicate = postJpaEntity.type.eq(PostType.ECONOMY_TALK);

        return new HashSet<>(
                jpaQueryFactory.select(postJpaEntity.title).from(postJpaEntity).where(predicate).fetch());
    }

    @Override
    public Page<ToktokWithScrapAndImageDTO> searchUsedToktokPosts(
            String keyword, Pageable pageable, long userId) {
        BooleanExpression predicate =
                postJpaEntity.type.eq(PostType.ECONOMY_TALK).and(postJpaEntity.usedDate.isNotNull());

        if (keyword != null && !keyword.trim().isEmpty()) {
            predicate =
                    predicate.and(
                            postJpaEntity.title.contains(keyword).or(postJpaEntity.content.contains(keyword)));
        }

        List<ToktokWithScrapAndImageDTO> content =
                jpaQueryFactory
                        .select(toktokPreviewProjection(userId))
                        .from(postJpaEntity)
                        .where(predicate)
                        .orderBy(postJpaEntity.usedDate.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        JPAQuery<Long> countQuery =
                jpaQueryFactory.select(postJpaEntity.count()).from(postJpaEntity).where(predicate);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public List<PostJpaEntity> findNewToktokPosts() {
        BooleanExpression predicate =
                postJpaEntity.type.eq(PostType.ECONOMY_TALK).and(postJpaEntity.usedDate.isNull());

        return jpaQueryFactory.selectFrom(postJpaEntity).where(predicate).fetch();
    }

    @Override
    public Page<ToktokWithScrapAndImageDTO> findUsedToktokPosts(
            Pageable pageable, PostSort postSort, long userId) {
        BooleanExpression predicate =
                postJpaEntity.type.eq(PostType.ECONOMY_TALK).and(postJpaEntity.usedDate.isNotNull());

        List<ToktokWithScrapAndImageDTO> content =
                jpaQueryFactory
                        .select(toktokPreviewProjection(userId))
                        .from(postJpaEntity)
                        .where(predicate)
                        .orderBy(getOrderSpecifiers(postSort))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        JPAQuery<Long> countQuery =
                jpaQueryFactory.select(postJpaEntity.count()).from(postJpaEntity).where(predicate);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Optional<ToktokWithScrapAndImageDTO> findByUsedDate(LocalDate usedDate, long userId) {
        BooleanExpression predicate =
                postJpaEntity.type.eq(PostType.ECONOMY_TALK).and(postJpaEntity.usedDate.eq(usedDate));

        ToktokWithScrapAndImageDTO dto =
                jpaQueryFactory
                        .select(toktokPreviewProjection(userId))
                        .from(postJpaEntity)
                        .where(predicate)
                        .fetchOne();

        return Optional.ofNullable(dto);
    }

    private ConstructorExpression<ToktokWithScrapAndImageDTO> toktokPreviewProjection(long userId) {
        return Projections.constructor(
                ToktokWithScrapAndImageDTO.class,
                postJpaEntity.id,
                postJpaEntity.title,
                postJpaEntity.content,
                postJpaEntity.type,
                postJpaEntity.likeCount,
                postJpaEntity.commentCount,
                postJpaEntity.scrapCount,
                JPAExpressions.select(imageJpaEntity.s3Info.url)
                        .from(imageJpaEntity)
                        .where(imageJpaEntity.postId.eq(postJpaEntity.id))
                        .orderBy(imageJpaEntity.id.asc())
                        .limit(1),
                JPAExpressions.select(postScrapJpaEntity.count())
                        .from(postScrapJpaEntity)
                        .where(
                                postScrapJpaEntity
                                        .postId
                                        .eq(postJpaEntity.id)
                                        .and(postScrapJpaEntity.userId.eq(userId)))
                        .gt(0L),
                postJpaEntity.usedDate);
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(PostSort postSort) {
        return (postSort == PostSort.POPULAR)
                ? new OrderSpecifier[] {postJpaEntity.likeCount.desc(), postJpaEntity.usedDate.desc()}
                : new OrderSpecifier[] {
                    postJpaEntity.usedDate != null
                            ? postJpaEntity.usedDate.desc()
                            : postJpaEntity.createdDate.desc()
                };
    }
}
