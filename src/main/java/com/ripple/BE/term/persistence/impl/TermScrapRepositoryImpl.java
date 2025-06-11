package com.ripple.BE.term.persistence.impl;

import com.ripple.BE.term.domain.TermScrap;
import com.ripple.BE.term.persistence.TermScrapRepository;
import com.ripple.BE.term.persistence.dto.TermWithScrapDTO;
import com.ripple.BE.term.persistence.jpa.entity.TermScrapJpaEntity;
import com.ripple.BE.term.persistence.jpa.repository.termscrap.TermScrapJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TermScrapRepositoryImpl implements TermScrapRepository {

    private final TermScrapJpaRepository termScrapJpaRepository;

    @Override
    public boolean existsByTermIdAndUserId(final long termId, final long userId) {
        return termScrapJpaRepository.existsByTermIdAndUserId(termId, userId);
    }

    @Override
    public Optional<TermScrap> findByTermIdAndUserId(final long termId, final long userId) {
        return termScrapJpaRepository
                .findByTermIdAndUserId(termId, userId)
                .map(TermScrapJpaEntity::toModel);
    }

    @Override
    public void save(final TermScrap termScrap) {
        termScrapJpaRepository.save(TermScrapJpaEntity.from(termScrap));
    }

    @Override
    public void delete(final TermScrap termScrap) {
        termScrapJpaRepository.delete(TermScrapJpaEntity.from(termScrap));
    }

    @Override
    public List<TermWithScrapDTO> findTermsScrappedByUserAndInitial(
            final long userId, final String initial) {
        return termScrapJpaRepository.findTermsScrappedByUserAndInitial(userId, initial);
    }

    @Override
    public List<TermWithScrapDTO> findTermsScrappedByUserAndKeyword(long userId, String keyword) {
        return termScrapJpaRepository.findTermsScrappedByUserAndKeyword(userId, keyword);
    }
}
