package com.ripple.BE.learning.persistence.jpa.repository.concept;

import com.ripple.BE.learning.persistence.jpa.entity.concept.ConceptJpaEntity;
import com.ripple.BE.user.domain.type.Level;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConceptJpaRepository extends JpaRepository<ConceptJpaEntity, Long> {

    int countByLevel(Level level);

    List<ConceptJpaEntity> findAllByLearningSetIdAndLevel(
            final long learningSetId, final Level level);
}
