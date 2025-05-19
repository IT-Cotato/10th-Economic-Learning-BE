package com.ripple.BE.learning.repository.quizScrap;

import static com.ripple.BE.learning.domain.quiz.QQuiz.*;
import static com.ripple.BE.learning.domain.quiz.QQuizScrap.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ripple.BE.learning.domain.quiz.Quiz;
import com.ripple.BE.user.domain.type.Level;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QuizScrapRepositoryCustomImpl implements QuizScrapRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Quiz> findQuizScrappedByUserAndLevel(Long userId, Level level) {
        return queryFactory
                .select(quiz)
                .from(quizScrap)
                .join(quizScrap.quiz, quiz)
                .where(quizScrap.user.id.eq(userId), quiz.level.eq(level))
                .fetch();
    }
}
