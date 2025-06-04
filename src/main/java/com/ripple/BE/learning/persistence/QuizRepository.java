package com.ripple.BE.learning.persistence;

import com.ripple.BE.learning.domain.quiz.Quiz;
import com.ripple.BE.user.domain.type.Level;
import java.util.List;
import java.util.Optional;

public interface QuizRepository {

    List<Quiz> findAllByLearningSetIdAndLevel(final long learningSetId, final Level level);

    Optional<Quiz> findById(final long quizId);

    List<Quiz> findAll();

    List<Quiz> findFailedQuizzesByUserAndLevel(long userId, Level level);

    void saveAll(final List<Quiz> quizzes);

    boolean existsById(final long quizId);
}
