package com.ripple.BE.learning.repository.quizScrap;

import com.ripple.BE.learning.domain.quiz.QuizScrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizScrapRepository
        extends JpaRepository<QuizScrap, Long>, QuizScrapRepositoryCustom {
    Boolean existsByQuizIdAndUserId(Long quizId, Long userId);
}
