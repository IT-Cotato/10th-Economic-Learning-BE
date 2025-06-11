package com.ripple.BE.term.persistence.jpa.repository.term;

import static com.ripple.BE.term.persistence.jpa.entity.QTermJpaEntity.*;
import static com.ripple.BE.term.persistence.jpa.entity.QTermScrapJpaEntity.*;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ripple.BE.term.persistence.dto.TermWithScrapDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class TermQueryRepositoryImpl implements TermQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<TermWithScrapDTO> findByInitial(String initial, Pageable pageable, long userId) {
        BooleanExpression predicate = termJpaEntity.initial.startsWith(initial);

        List<TermWithScrapDTO> termList = getTermsWithScrapByPageable(pageable, predicate, userId);

        JPAQuery<Long> countQuery =
                queryFactory.select(termJpaEntity.count()).from(termJpaEntity).where(predicate);

        return PageableExecutionUtils.getPage(termList, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<TermWithScrapDTO> findAllWithoutKeyword(Pageable pageable, long userId) {

        List<TermWithScrapDTO> termList = getTermsWithScrapByPageable(pageable, null, userId);

        JPAQuery<Long> countQuery = queryFactory.select(termJpaEntity.count()).from(termJpaEntity);

        return PageableExecutionUtils.getPage(termList, pageable, countQuery::fetchOne);
    }

    private List<TermWithScrapDTO> getTermsWithScrapByPageable(
            Pageable pageable, BooleanExpression predicate, long userId) {

        return queryFactory
                .select(
                        Projections.constructor(
                                TermWithScrapDTO.class,
                                termJpaEntity.id,
                                termJpaEntity.title,
                                termJpaEntity.description,
                                termJpaEntity.initial,
                                Expressions.asBoolean(termScrapJpaEntity.id.isNotNull())))
                .from(termJpaEntity)
                .leftJoin(termScrapJpaEntity)
                .on(
                        termJpaEntity
                                .id
                                .eq(termScrapJpaEntity.termId)
                                .and(termScrapJpaEntity.userId.eq(userId)))
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
