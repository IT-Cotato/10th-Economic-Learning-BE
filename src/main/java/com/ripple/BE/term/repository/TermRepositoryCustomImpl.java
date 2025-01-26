package com.ripple.BE.term.repository;

import static com.ripple.BE.term.domain.QTerm.*;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
    public Page<Term> findByInitial(String initial, Pageable pageable) {
        BooleanExpression predicate = term.initial.startsWith(initial);

        List<Term> termList = getTermByPageable(pageable, predicate);

        JPAQuery<Long> countQuery = queryFactory.select(term.count()).from(term).where(predicate);

        return PageableExecutionUtils.getPage(termList, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Term> findByKeyword(String keyword, Pageable pageable) {
        BooleanExpression predicate = null;

        if (keyword != null && !keyword.trim().isEmpty()) {
            predicate = term.title.containsIgnoreCase(keyword);
        }

        List<Term> termList = getTermByPageable(pageable, predicate);

        JPAQuery<Long> countQuery = queryFactory.select(term.count()).from(term).where(predicate);

        return PageableExecutionUtils.getPage(termList, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Term> searchTerms(String keyword, Pageable pageable) {
        BooleanExpression predicate = null;

        if (keyword != null && !keyword.trim().isEmpty()) {
            predicate =
                    term.title.containsIgnoreCase(keyword).or(term.description.containsIgnoreCase(keyword));
        }

        List<Term> termList = getTermByPageable(pageable, predicate);

        JPAQuery<Long> countQuery = queryFactory.select(term.count()).from(term).where(predicate);

        return PageableExecutionUtils.getPage(termList, pageable, countQuery::fetchOne);
    }

    private List<Term> getTermByPageable(Pageable pageable, BooleanExpression predicate) {
        return queryFactory
                .selectFrom(term)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
