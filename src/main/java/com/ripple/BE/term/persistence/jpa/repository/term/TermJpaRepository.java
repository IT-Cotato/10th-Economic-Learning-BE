package com.ripple.BE.term.persistence.jpa.repository.term;

import com.ripple.BE.term.persistence.jpa.entity.TermJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermJpaRepository
        extends JpaRepository<TermJpaEntity, Long>, TermQueryRepository {}
