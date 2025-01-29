package com.ripple.BE.news.repository.news;

import static com.ripple.BE.news.domain.QNews.*;
import static com.ripple.BE.news.domain.QNewsScrap.*;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ripple.BE.news.domain.News;
import com.ripple.BE.news.domain.QNews;
import com.ripple.BE.news.domain.type.NewsCategory;
import com.ripple.BE.news.domain.type.NewsSort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class NewsRepositoryCustomImpl implements NewsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<News> findByCategory(
            NewsCategory category, NewsSort newsSort, Pageable pageable, long userId) {
        BooleanExpression predicate = news.category.eq(category);

        List<News> newsList = getNewsWithScrapByPageable(pageable, predicate, newsSort, userId);

        JPAQuery<Long> countQuery = queryFactory.select(news.count()).from(news).where(predicate);

        return PageableExecutionUtils.getPage(newsList, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<News> searchNews(String keyword, Pageable pageable, long userId) {
        BooleanExpression predicate = null;

        if (keyword != null && !keyword.trim().isEmpty()) {
            predicate = news.title.contains(keyword).or(news.content.contains(keyword));
        }

        List<News> newsList = getNewsWithScrapByPageable(pageable, predicate, NewsSort.RECENT, userId);

        JPAQuery<Long> countQuery = queryFactory.select(news.count()).from(news).where(predicate);

        return PageableExecutionUtils.getPage(newsList, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<News> findAll(Pageable pageable, NewsSort newsSort, long userId) {

        List<News> newsList = getNewsWithScrapByPageable(pageable, null, newsSort, userId);

        JPAQuery<Long> countQuery = queryFactory.select(news.count()).from(news);

        return PageableExecutionUtils.getPage(newsList, pageable, countQuery::fetchOne);
    }

    private List<News> getNewsWithScrapByPageable(
            Pageable pageable, BooleanExpression predicate, NewsSort newsSort, long userId) {

        // 동적으로 정렬 조건 설정
        var orderBy =
                (newsSort == NewsSort.POPULAR)
                        ? new com.querydsl.core.types.OrderSpecifier[] {
                            news.views.desc(), news.createdDate.desc()
                        }
                        : new com.querydsl.core.types.OrderSpecifier[] {news.createdDate.desc()};

        List<Tuple> results =
                queryFactory
                        .select(news, newsScrap.id)
                        .from(news)
                        .leftJoin(newsScrap)
                        .on(news.id.eq(newsScrap.news.id).and(newsScrap.user.id.eq(userId)))
                        .where(predicate)
                        .orderBy(orderBy)
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        return results.stream()
                .map(
                        tuple -> {
                            News news = tuple.get(QNews.news);
                            Long scrapId = tuple.get(newsScrap.id);
                            news.setIsScrapped(scrapId != null);
                            return news;
                        })
                .toList();
    }
}
