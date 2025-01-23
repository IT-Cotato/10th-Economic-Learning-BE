package com.ripple.BE.learning.repository.learningSet;

import com.ripple.BE.learning.domain.learningset.LearningSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LearningSetRepository extends JpaRepository<LearningSet, Long> {}
