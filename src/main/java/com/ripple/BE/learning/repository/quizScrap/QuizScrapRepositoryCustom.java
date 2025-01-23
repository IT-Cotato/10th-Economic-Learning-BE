package com.ripple.BE.learning.repository.quizScrap;

import com.ripple.BE.learning.domain.quiz.Quiz;
import com.ripple.BE.user.domain.type.Level;
import java.util.List;

public interface QuizScrapRepositoryCustom {

    List<Quiz> findQuizScrappedByUserAndLevel(Long userId, Level level);
}
