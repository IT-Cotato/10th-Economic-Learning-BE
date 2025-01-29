package com.ripple.BE.learning.repository.quiz;

import com.ripple.BE.learning.domain.quiz.Quiz;
import com.ripple.BE.user.domain.type.Level;
import java.util.List;

public interface QuizRepositoryCustom {

    List<Quiz> findFailedQuizzesByUserAndLevel(long userId, Level level);
}
