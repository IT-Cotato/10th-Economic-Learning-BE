package com.ripple.BE.learning.persistence.impl;

import com.ripple.BE.learning.domain.concept.Concept;
import com.ripple.BE.learning.domain.concept.ConceptScrap;
import com.ripple.BE.learning.persistence.ConceptScrapRepository;
import com.ripple.BE.learning.persistence.jpa.entity.concept.ConceptJpaEntity;
import com.ripple.BE.learning.persistence.jpa.entity.concept.ConceptScrapJpaEntity;
import com.ripple.BE.learning.persistence.jpa.repository.conceptscrap.ConceptScrapJpaRepository;
import com.ripple.BE.user.domain.type.Level;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ConceptScrapRepositoryImpl implements ConceptScrapRepository {

    private final ConceptScrapJpaRepository conceptScrapJpaRepository;

    @Override
    public boolean existsByUserIdAndConceptId(final long userId, final long conceptId) {

        return conceptScrapJpaRepository.existsByUserIdAndConceptId(userId, conceptId);
    }

    @Override
    public ConceptScrap save(final ConceptScrap conceptScrap) {
        return conceptScrapJpaRepository.save(ConceptScrapJpaEntity.from(conceptScrap)).toModel();
    }

    @Override
    public Optional<ConceptScrap> findByUserIdAndConceptId(final long userId, final long conceptId) {
        return conceptScrapJpaRepository
                .findByUserIdAndConceptId(userId, conceptId)
                .map(ConceptScrapJpaEntity::toModel);
    }

    @Override
    public void delete(final ConceptScrap conceptScrap) {
        conceptScrapJpaRepository.deleteByUserIdAndConceptId(
                conceptScrap.getUserId(), conceptScrap.getConceptId());
    }

    @Override
    public List<Concept> findConceptsScrappedByUserAndLevel(final long userId, final Level level) {
        return conceptScrapJpaRepository.findConceptsScrappedByUserAndLevel(userId, level).stream()
                .map(ConceptJpaEntity::toModel)
                .toList();
    }
}
