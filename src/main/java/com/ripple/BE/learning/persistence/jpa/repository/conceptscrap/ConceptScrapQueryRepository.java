package com.ripple.BE.learning.persistence.jpa.repository.conceptscrap;

import com.ripple.BE.learning.persistence.jpa.entity.concept.ConceptJpaEntity;
import com.ripple.BE.user.domain.type.Level;
import java.util.List;

public interface ConceptScrapQueryRepository {

    List<ConceptJpaEntity> findConceptsScrappedByUserAndLevel(Long userId, Level level);
}
