package com.ripple.BE.term.repository;

import static com.ripple.BE.term.domain.QTerm.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ripple.BE.term.domain.Term;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TermRepositoryCustomImpl implements TermRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Term> findByInitial(String initial) {
        return queryFactory
                .selectFrom(term)
                .where(term.initial.startsWith(initial)) // 초성의 첫 글자가 일치하는지 확인
                .fetch();
    }

    @Override
    public List<Term> findByKeyword(String keyword) {
        return queryFactory
                .selectFrom(term)
                .where(
                        term.title.containsIgnoreCase(keyword) // 제목에서 검색
                        )
                .fetch();
    }
}
