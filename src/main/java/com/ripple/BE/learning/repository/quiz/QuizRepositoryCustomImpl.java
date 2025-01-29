package com.ripple.BE.learning.repository.quiz;

import static com.ripple.BE.learning.domain.quiz.QFailQuiz.*;
import static com.ripple.BE.learning.domain.quiz.QQuiz.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ripple.BE.learning.domain.quiz.Quiz;
import com.ripple.BE.user.domain.type.Level;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QuizRepositoryCustomImpl implements QuizRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Quiz> findFailedQuizzesByUserAndLevel(long userId, Level level) {
        return queryFactory
                .select(quiz)
                .from(failQuiz)
                .join(failQuiz.quiz, quiz)
                .where(failQuiz.user.id.eq(userId), quiz.level.eq(level))
                .fetch();
    }
}
