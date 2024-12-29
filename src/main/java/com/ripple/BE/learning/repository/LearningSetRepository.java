package com.ripple.BE.learning.repository;

import com.ripple.BE.learning.domain.LearningSet;
import com.ripple.BE.user.domain.type.Level;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LearningSetRepository extends JpaRepository<LearningSet, Long> {

    @Query("SELECT l.id FROM LearningSet l WHERE l.level = :level")
    List<Long> findIdsByLevel(@Param("level") Level level);

    @Query("SELECT ls.learningSetNum FROM LearningSet ls")
    List<String> findAllLearningSetNums();
}
