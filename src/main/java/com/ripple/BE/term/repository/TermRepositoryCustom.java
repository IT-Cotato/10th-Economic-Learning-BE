package com.ripple.BE.term.repository;

import com.ripple.BE.term.domain.Term;
import java.util.List;

public interface TermRepositoryCustom {

    List<Term> findByInitial(String initial);

    List<Term> findByKeyword(String keyword);
}
