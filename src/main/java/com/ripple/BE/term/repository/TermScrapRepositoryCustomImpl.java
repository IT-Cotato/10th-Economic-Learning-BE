package com.ripple.BE.term.repository;

import static com.ripple.BE.term.domain.QTerm.*;
import static com.ripple.BE.term.domain.QTermScrap.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ripple.BE.term.domain.Term;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TermScrapRepositoryCustomImpl implements TermScrapRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Term> findTermsScrappedByUserAndInitial(Long userId, String initial) {
        if (initial == null || initial.trim().isEmpty()) {
            return queryFactory
                    .select(term)
                    .from(termScrap)
                    .join(termScrap.term, term)
                    .where(termScrap.user.id.eq(userId))
                    .orderBy(termScrap.createdDate.desc())
                    .fetch();
        }

        return queryFactory
                .select(term)
                .from(termScrap)
                .join(termScrap.term, term)
                .where(termScrap.user.id.eq(userId).and(term.initial.startsWith(initial)))
                .orderBy(termScrap.createdDate.desc())
                .fetch();
    }

    @Override
    public List<Term> findTermsScrappedByUserAndKeyword(Long userId, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return queryFactory
                    .select(term)
                    .from(termScrap)
                    .join(termScrap.term, term)
                    .where(termScrap.user.id.eq(userId))
                    .orderBy(termScrap.createdDate.desc())
                    .fetch();
        }

        return queryFactory
                .select(term)
                .from(termScrap)
                .join(termScrap.term, term)
                .where(termScrap.user.id.eq(userId).and(term.title.containsIgnoreCase(keyword)))
                .orderBy(termScrap.createdDate.desc())
                .fetch();
    }
}
