package com.ripple.BE.term.persistence.jpa.repository.termscrap;

import com.ripple.BE.term.persistence.dto.TermWithScrapDTO;
import java.util.List;

public interface TermScrapQueryRepository {

    List<TermWithScrapDTO> findTermsScrappedByUserAndInitial(Long userId, String initial);

    List<TermWithScrapDTO> findTermsScrappedByUserAndKeyword(Long userId, String keyword);
}
