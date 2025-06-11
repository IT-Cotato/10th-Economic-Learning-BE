package com.ripple.BE.learning.persistence;

import com.ripple.BE.learning.domain.concept.Concept;
import com.ripple.BE.learning.domain.concept.ConceptScrap;
import com.ripple.BE.user.domain.type.Level;
import java.util.List;
import java.util.Optional;

public interface ConceptScrapRepository {

    boolean existsByUserIdAndConceptId(final long userId, final long conceptId);

    ConceptScrap save(final ConceptScrap conceptScrap);

    Optional<ConceptScrap> findByUserIdAndConceptId(final long userId, final long conceptId);

    void delete(final ConceptScrap conceptScrap);

    List<Concept> findConceptsScrappedByUserAndLevel(final long userId, final Level level);
}
