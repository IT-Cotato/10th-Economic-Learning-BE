package com.ripple.BE.learning.persistence.jpa.repository.quizscrap;

import static com.ripple.BE.learning.persistence.jpa.entity.quiz.QQuizJpaEntity.*;
import static com.ripple.BE.learning.persistence.jpa.entity.quiz.QQuizScrapJpaEntity.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ripple.BE.learning.persistence.jpa.entity.quiz.QuizJpaEntity;
import com.ripple.BE.user.domain.type.Level;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QuizScrapQueryRepositoryImpl implements QuizScrapQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<QuizJpaEntity> findQuizScrappedByUserIdAndLevel(Long userId, Level level) {
        return queryFactory
                .select(quizJpaEntity)
                .from(quizScrapJpaEntity)
                .join(quizJpaEntity)
                .on(quizJpaEntity.id.eq(quizScrapJpaEntity.quizId))
                .where(quizScrapJpaEntity.userId.eq(userId), quizJpaEntity.level.eq(level))
                .fetch();
    }
}
