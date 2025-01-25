package com.ripple.BE.term.repository;

import com.ripple.BE.term.domain.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermRepository extends JpaRepository<Term, Long>, TermRepositoryCustom {}
