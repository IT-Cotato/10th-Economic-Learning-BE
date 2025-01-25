package com.ripple.BE.learning.repository.conceptScrap;

import com.ripple.BE.learning.domain.concept.Concept;
import com.ripple.BE.user.domain.type.Level;
import java.util.List;

public interface ConceptScrapRepositoryCustom {

    List<Concept> findConceptsScrappedByUserAndLevel(Long userId, Level level);
}
