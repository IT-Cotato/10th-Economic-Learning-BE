package com.ripple.BE.learning.repository;

import com.ripple.BE.learning.domain.learningset.LearningSet;
import com.ripple.BE.learning.domain.quiz.Quiz;
import com.ripple.BE.learning.domain.type.Purpose;
import com.ripple.BE.user.domain.type.Level;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    int countByLevel(Level level);

    List<Quiz> findAllByLearningSetAndLevel(LearningSet learningSet, Level level);

    List<Quiz> findAllByPurpose(Purpose purpose);
}
