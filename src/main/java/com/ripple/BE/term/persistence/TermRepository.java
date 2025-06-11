package com.ripple.BE.term.persistence;

import com.ripple.BE.term.domain.Term;
import com.ripple.BE.term.persistence.dto.TermWithScrapDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TermRepository {

    Page<TermWithScrapDTO> findByInitial(String consonant, Pageable pageable, long userId);

    Page<TermWithScrapDTO> findByKeyword(String keyword, Pageable pageable, long userId);

    Page<TermWithScrapDTO> searchTerms(String keyword, Pageable pageable, long userId);

    Optional<Term> findById(long termId);

    List<Term> findAll();

    void saveAllTerms(List<Term> terms);
}
