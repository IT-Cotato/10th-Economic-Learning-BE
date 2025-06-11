package com.ripple.BE.news.persistence.jpa.repository.newsscrap;

import static com.ripple.BE.news.persistence.jpa.entity.QNewsJpaEntity.*;
import static com.ripple.BE.news.persistence.jpa.entity.QNewsScrapJpaEntity.*;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ripple.BE.news.persistence.dto.NewsWithScrapDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NewsScrapQueryRepositoryImpl implements NewsScrapQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<NewsWithScrapDTO> findNewsScrappedByUser(Long userId) {
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
                                Expressions.asBoolean(true)))
                .from(newsScrapJpaEntity)
                .join(newsJpaEntity)
                .on(newsScrapJpaEntity.newsId.eq(newsJpaEntity.id))
                .where(newsScrapJpaEntity.userId.eq(userId))
                .orderBy(newsScrapJpaEntity.createdDate.desc())
                .fetch();
    }
}
