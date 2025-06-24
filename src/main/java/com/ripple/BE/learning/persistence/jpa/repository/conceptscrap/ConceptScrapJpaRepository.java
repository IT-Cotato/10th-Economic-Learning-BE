package com.ripple.BE.learning.persistence.jpa.repository.conceptscrap;

import com.ripple.BE.learning.persistence.jpa.entity.concept.ConceptScrapJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConceptScrapJpaRepository
        extends JpaRepository<ConceptScrapJpaEntity, Long>, ConceptScrapQueryRepository {
    Boolean existsByUserIdAndConceptId(Long userId, Long conceptId);

    void deleteByUserIdAndConceptId(Long userId, Long conceptId);

    Optional<ConceptScrapJpaEntity> findByUserIdAndConceptId(Long userId, Long conceptId);

    void deleteAllByUserId(Long userId);
}
