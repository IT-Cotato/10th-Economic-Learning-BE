package com.ripple.BE.term.persistence.jpa.repository.termscrap;

import static com.ripple.BE.term.persistence.jpa.entity.QTermJpaEntity.termJpaEntity;
import static com.ripple.BE.term.persistence.jpa.entity.QTermScrapJpaEntity.termScrapJpaEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ripple.BE.term.persistence.dto.TermWithScrapDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TermScrapQueryRepositoryImpl implements TermScrapQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<TermWithScrapDTO> findTermsScrappedByUserAndInitial(Long userId, String initial) {
        var query =
                queryFactory
                        .select(
                                Projections.constructor(
                                        TermWithScrapDTO.class,
                                        termJpaEntity.id,
                                        termJpaEntity.title,
                                        termJpaEntity.description,
                                        termJpaEntity.initial,
                                        Expressions.asBoolean(true)))
                        .from(termScrapJpaEntity)
                        .join(termJpaEntity)
                        .on(termScrapJpaEntity.termId.eq(termJpaEntity.id))
                        .where(termScrapJpaEntity.userId.eq(userId))
                        .orderBy(termScrapJpaEntity.createdDate.desc());

        if (initial != null && !initial.trim().isEmpty()) {
            query.where(termJpaEntity.initial.startsWith(initial));
        }

        return query.fetch();
    }

    @Override
    public List<TermWithScrapDTO> findTermsScrappedByUserAndKeyword(Long userId, String keyword) {
        var query =
                queryFactory
                        .select(
                                Projections.constructor(
                                        TermWithScrapDTO.class,
                                        termJpaEntity.id,
                                        termJpaEntity.title,
                                        termJpaEntity.description,
                                        termJpaEntity.initial,
                                        Expressions.asBoolean(true)))
                        .from(termScrapJpaEntity)
                        .join(termJpaEntity)
                        .on(termScrapJpaEntity.termId.eq(termJpaEntity.id))
                        .where(termScrapJpaEntity.userId.eq(userId))
                        .orderBy(termScrapJpaEntity.createdDate.desc());

        if (keyword != null && !keyword.trim().isEmpty()) {
            query.where(termJpaEntity.title.containsIgnoreCase(keyword));
        }

        return query.fetch();
    }
}
