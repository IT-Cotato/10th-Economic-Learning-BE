package com.ripple.BE.learning.repository.conceptScrap;

import com.ripple.BE.learning.domain.concept.ConceptScrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConceptScrapRepository extends JpaRepository<ConceptScrap, Long> {
    Boolean existsByConceptIdAndUserId(Long conceptId, Long userId);
}
