package com.ripple.BE.term.repository;

import com.ripple.BE.term.domain.Term;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TermRepositoryCustom {

    Page<Term> findByInitial(String initial, Pageable pageable);

    Page<Term> findByKeyword(String keyword, Pageable pageable);

    Page<Term> searchTerms(String keyword, Pageable pageable);
}
