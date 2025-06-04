package com.ripple.BE.learning.persistence.jpa.repository.quiz;

import com.ripple.BE.learning.persistence.jpa.entity.quiz.QuizJpaEntity;
import com.ripple.BE.user.domain.type.Level;
import java.util.List;

public interface QuizQueryRepository {

    List<QuizJpaEntity> findFailedQuizzesByUserIdAndLevel(long userId, Level level);
}
