package com.ripple.BE.learning.repository;

import com.ripple.BE.learning.domain.learningset.LearningSet;
import com.ripple.BE.learning.domain.quiz.Quiz;
import com.ripple.BE.learning.domain.type.Purpose;
import com.ripple.BE.user.domain.type.Level;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    int countByLevel(Level level);

    @Query(
            "SELECT q FROM Quiz q "
                    + "LEFT JOIN FETCH q.choices "
                    + "WHERE q.learningSet = :learningSet AND q.level = :level")
    List<Quiz> findAllByLearningSetAndLevel(
            @Param("learningSet") LearningSet learningSet, @Param("level") Level level);

    List<Quiz> findAllByPurpose(Purpose purpose);
}
