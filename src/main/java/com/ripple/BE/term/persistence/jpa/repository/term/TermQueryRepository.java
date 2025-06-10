package com.ripple.BE.term.persistence.jpa.repository.term;

import com.ripple.BE.term.persistence.dto.TermWithScrapDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TermQueryRepository {

    Page<TermWithScrapDTO> findByInitial(String initial, Pageable pageable, long userId);

    Page<TermWithScrapDTO> findAllWithoutKeyword(Pageable pageable, long userId);
}
