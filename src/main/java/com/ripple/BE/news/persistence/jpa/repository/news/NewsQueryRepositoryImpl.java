package com.ripple.BE.news.persistence.jpa.repository.news;

import static com.ripple.BE.news.persistence.jpa.entity.QNewsJpaEntity.*;
import static com.ripple.BE.news.persistence.jpa.entity.QNewsScrapJpaEntity.*;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ripple.BE.news.domain.type.NewsCategory;
import com.ripple.BE.news.domain.type.NewsSort;
import com.ripple.BE.news.persistence.dto.NewsWithScrapDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class NewsQueryRepositoryImpl implements NewsQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<NewsWithScrapDTO> findByCategory(
            NewsCategory category, NewsSort newsSort, Pageable pageable, long userId) {

        BooleanExpression predicate = newsJpaEntity.category.eq(category);

        List<NewsWithScrapDTO> results =
                getNewsWithScrapByPageable(pageable, predicate, newsSort, userId);

        JPAQuery<Long> countQuery =
                queryFactory.select(newsJpaEntity.count()).from(newsJpaEntity).where(predicate);

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<NewsWithScrapDTO> searchNews(String keyword, Pageable pageable, long userId) {
        BooleanExpression predicate = null;

        if (keyword != null && !keyword.trim().isEmpty()) {
            predicate = newsJpaEntity.title.contains(keyword).or(newsJpaEntity.content.contains(keyword));
        }

        List<NewsWithScrapDTO> results =
                getNewsWithScrapByPageable(pageable, predicate, NewsSort.RECENT, userId);

        JPAQuery<Long> countQuery =
                queryFactory.select(newsJpaEntity.count()).from(newsJpaEntity).where(predicate);

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<NewsWithScrapDTO> findAll(Pageable pageable, NewsSort newsSort, long userId) {
        List<NewsWithScrapDTO> results = getNewsWithScrapByPageable(pageable, null, newsSort, userId);

        JPAQuery<Long> countQuery = queryFactory.select(newsJpaEntity.count()).from(newsJpaEntity);

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }

    private List<NewsWithScrapDTO> getNewsWithScrapByPageable(
            Pageable pageable, BooleanExpression predicate, NewsSort newsSort, long userId) {

        OrderSpecifier<?>[] orderBy =
                (newsSort == NewsSort.POPULAR)
                        ? new OrderSpecifier[] {newsJpaEntity.views.desc(), newsJpaEntity.pubDate.desc()}
                        : new OrderSpecifier[] {newsJpaEntity.pubDate.desc()};

        return queryFactory
                .select(
                        Projections.constructor(
                                NewsWithScrapDTO.class,
                                newsJpaEntity.id,
                                newsJpaEntity.title,
                                newsJpaEntity.content,
                                newsJpaEntity.publisher,
                                newsJpaEntity.url,
                                newsJpaEntity.category,
                                newsJpaEntity.views,
                                newsJpaEntity.pubDate,
                                newsScrapJpaEntity.id.isNotNull()))
                .from(newsJpaEntity)
                .leftJoin(newsScrapJpaEntity)
                .on(
                        newsJpaEntity
                                .id
                                .eq(newsScrapJpaEntity.newsId)
                                .and(newsScrapJpaEntity.userId.eq(userId)))
                .where(predicate)
                .orderBy(orderBy)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
