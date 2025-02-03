package com.ripple.BE.term.repository;

import com.ripple.BE.term.domain.Term;
import java.util.List;

public interface TermScrapRepositoryCustom {

    List<Term> findTermsScrappedByUserAndInitial(Long userId, String initial);

    List<Term> findTermsScrappedByUserAndKeyword(Long userId, String keyword);
}
