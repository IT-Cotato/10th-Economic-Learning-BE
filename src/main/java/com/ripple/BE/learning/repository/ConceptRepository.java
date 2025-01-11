package com.ripple.BE.learning.repository;

import com.ripple.BE.learning.domain.concept.Concept;
import com.ripple.BE.learning.domain.learningset.LearningSet;
import com.ripple.BE.user.domain.type.Level;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConceptRepository extends JpaRepository<Concept, Long> {

    int countByLevel(Level level);

    List<Concept> findAllByLearningSetAndLevel(LearningSet learningSet, Level level);
}
