package com.ripple.BE.news.repository.news;

import static com.ripple.BE.news.domain.QNews.*;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ripple.BE.news.domain.News;
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
    public Page<News> findByCategory(NewsCategory category, NewsSort newsSort, Pageable pageable) {
        BooleanExpression predicate = news.category.eq(category);

        List<News> newsList = getNewsByPageable(pageable, predicate, newsSort);

        JPAQuery<Long> countQuery = queryFactory.select(news.count()).from(news).where(predicate);

        return PageableExecutionUtils.getPage(newsList, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<News> searchNews(String keyword, Pageable pageable) {
        BooleanExpression predicate = null;

        if (keyword != null && !keyword.trim().isEmpty()) {
            predicate = news.title.contains(keyword).or(news.content.contains(keyword));
        }

        List<News> newsList = getNewsByPageable(pageable, predicate, NewsSort.RECENT);

        JPAQuery<Long> countQuery = queryFactory.select(news.count()).from(news).where(predicate);

        return PageableExecutionUtils.getPage(newsList, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<News> findAll(Pageable pageable, NewsSort newsSort) {

        List<News> newsList = getNewsByPageable(pageable, null, newsSort);

        JPAQuery<Long> countQuery = queryFactory.select(news.count()).from(news);

        return PageableExecutionUtils.getPage(newsList, pageable, countQuery::fetchOne);
    }

    private List<News> getNewsByPageable(
            Pageable pageable, BooleanExpression predicate, NewsSort newsSort) {
        if (newsSort == NewsSort.POPULAR) {
            return queryFactory
                    .selectFrom(news)
                    .where(predicate)
                    .orderBy(
                            news.views.desc(), // 조회수 내림차순
                            news.createdDate.desc() // 생성일 내림차순
                            )
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        } else {
            return queryFactory
                    .selectFrom(news)
                    .where(predicate)
                    .orderBy(news.createdDate.desc()) // 생성일 내림차순
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        }
    }
}
