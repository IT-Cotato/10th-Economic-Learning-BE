package com.ripple.BE.learning.persistence;

import com.ripple.BE.learning.domain.learningset.LearningSet;
import java.util.List;
import java.util.Optional;

public interface LearningSetRepository {

    Optional<LearningSet> findById(final long learningSetId);

    List<LearningSet> findAll();

    List<LearningSet> saveAll(final List<LearningSet> learningSets);
}
