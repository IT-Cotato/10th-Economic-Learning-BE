package com.ripple.BE.term.persistence;

import com.ripple.BE.term.domain.TermScrap;
import com.ripple.BE.term.persistence.dto.TermWithScrapDTO;
import java.util.List;
import java.util.Optional;

public interface TermScrapRepository {

    boolean existsByTermIdAndUserId(long termId, long userId);

    Optional<TermScrap> findByTermIdAndUserId(long termId, long userId);

    void save(TermScrap termScrap);

    void delete(TermScrap termScrap);

    List<TermWithScrapDTO> findTermsScrappedByUserAndInitial(long userId, String initial);

    List<TermWithScrapDTO> findTermsScrappedByUserAndKeyword(long userId, String keyword);

    void deleteAllByUserId(long userId);
}
