package com.ripple.BE.news.repository.newscrap;

import static com.ripple.BE.news.domain.QNews.*;
import static com.ripple.BE.news.domain.QNewsScrap.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ripple.BE.news.domain.News;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NewsScrapRepositoryCustomImpl implements NewsScrapRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<News> findNewsScrappedByUser(Long userId) {
        return queryFactory
                .select(news)
                .from(newsScrap)
                .join(newsScrap.news, news)
                .where(newsScrap.user.id.eq(userId))
                .orderBy(newsScrap.createdDate.desc())
                .fetch();
    }
}
