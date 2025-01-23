package com.ripple.BE.learning.repository.quizScrap;

import com.ripple.BE.learning.domain.quiz.Quiz;
import java.util.List;

public interface QuizScrapRepositoryCustom {

    List<Quiz> findQuizScrappedByUser(Long userId);
}
