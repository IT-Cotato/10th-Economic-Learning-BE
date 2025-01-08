package com.ripple.BE.learning.repository;

import com.ripple.BE.learning.domain.LearningSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LearningSetRepository extends JpaRepository<LearningSet, Long> {}
