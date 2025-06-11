package com.ripple.BE.learning.persistence.jpa.repository.quizscrap;

import com.ripple.BE.learning.persistence.jpa.entity.quiz.QuizJpaEntity;
import com.ripple.BE.user.domain.type.Level;
import java.util.List;

public interface QuizScrapQueryRepository {

    List<QuizJpaEntity> findQuizScrappedByUserIdAndLevel(Long userId, Level level);
}
