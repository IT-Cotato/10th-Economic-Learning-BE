package com.ripple.BE.learning.persistence;

import com.ripple.BE.learning.domain.concept.Concept;
import com.ripple.BE.user.domain.type.Level;
import java.util.List;
import java.util.Optional;

public interface ConceptRepository {

    List<Concept> findAllByLearningSetIdAndLevel(final long learningSetId, final Level level);

    Optional<Concept> findById(final long id);

    List<Concept> saveAll(final List<Concept> concepts);

    boolean existsById(final long id);
}
