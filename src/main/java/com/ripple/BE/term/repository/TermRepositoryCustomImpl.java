package com.ripple.BE.term.repository;

import static com.ripple.BE.term.domain.QTerm.*;
import static com.ripple.BE.term.domain.QTermScrap.*;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ripple.BE.term.domain.QTerm;
import com.ripple.BE.term.domain.Term;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class TermRepositoryCustomImpl implements TermRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Term> findByInitial(String initial, Pageable pageable, long userId) {
        BooleanExpression predicate = term.initial.startsWith(initial);

        List<Term> termList = getTermsWithScrapByPageable(pageable, predicate, userId);

        JPAQuery<Long> countQuery = queryFactory.select(term.count()).from(term).where(predicate);

        return PageableExecutionUtils.getPage(termList, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Term> findByKeyword(String keyword, Pageable pageable, long userId) {
        BooleanExpression predicate = null;

        if (keyword != null && !keyword.trim().isEmpty()) {
            predicate = term.title.containsIgnoreCase(keyword);
        }

        List<Term> termList = getTermsWithScrapByPageable(pageable, predicate, userId);

        JPAQuery<Long> countQuery = queryFactory.select(term.count()).from(term).where(predicate);

        return PageableExecutionUtils.getPage(termList, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Term> searchTerms(String keyword, Pageable pageable, long userId) {
        BooleanExpression predicate = null;

        if (keyword != null && !keyword.trim().isEmpty()) {
            predicate =
                    term.title.containsIgnoreCase(keyword).or(term.description.containsIgnoreCase(keyword));
        }

        List<Term> termList = getTermsWithScrapByPageable(pageable, predicate, userId);

        JPAQuery<Long> countQuery = queryFactory.select(term.count()).from(term).where(predicate);

        return PageableExecutionUtils.getPage(termList, pageable, countQuery::fetchOne);
    }

    private List<Term> getTermsWithScrapByPageable(
            Pageable pageable, BooleanExpression predicate, long userId) {

        // 쿼리 실행: 스크랩 여부를 포함하여 데이터를 한 번에 가져옴
        List<Tuple> results =
                queryFactory
                        .select(term, termScrap.id)
                        .from(term)
                        .leftJoin(termScrap)
                        .on(term.id.eq(termScrap.term.id).and(termScrap.user.id.eq(userId)))
                        .where(predicate)
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        return results.stream()
                .map(
                        tuple -> {
                            Term term = tuple.get(QTerm.term);
                            Long scrapId = tuple.get(termScrap.id);
                            term.setIsScrapped(scrapId != null);
                            return term;
                        })
                .toList();
    }
}
