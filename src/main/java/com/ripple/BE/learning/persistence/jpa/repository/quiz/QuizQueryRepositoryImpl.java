package com.ripple.BE.learning.persistence.jpa.repository.quiz;

import static com.ripple.BE.learning.persistence.jpa.entity.quiz.QFailQuizJpaEntity.*;
import static com.ripple.BE.learning.persistence.jpa.entity.quiz.QQuizJpaEntity.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ripple.BE.learning.persistence.jpa.entity.quiz.QuizJpaEntity;
import com.ripple.BE.user.domain.type.Level;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QuizQueryRepositoryImpl implements QuizQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<QuizJpaEntity> findFailedQuizzesByUserIdAndLevel(long userId, Level level) {
        return queryFactory
                .select(quizJpaEntity)
                .from(failQuizJpaEntity)
                .join(quizJpaEntity)
                .on(failQuizJpaEntity.quizId.eq(quizJpaEntity.id))
                .where(failQuizJpaEntity.userId.eq(userId), quizJpaEntity.level.eq(level))
                .fetch();
    }
}
