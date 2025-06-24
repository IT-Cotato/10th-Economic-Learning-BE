package com.ripple.BE.learning.persistence;

import com.ripple.BE.learning.domain.quiz.Quiz;
import com.ripple.BE.learning.domain.quiz.QuizScrap;
import com.ripple.BE.user.domain.type.Level;
import java.util.List;
import java.util.Optional;

public interface QuizScrapRepository {

    Optional<QuizScrap> findByQuizIdAndUserId(final long quizId, final long userId);

    void delete(final QuizScrap quizScrap);

    boolean existsByQuizIdAndUserId(final long quizId, final long userId);

    QuizScrap save(final QuizScrap quizScrap);

    List<Quiz> findQuizScrappedByUserIdAndLevel(Long userId, Level level);

    void deleteAllByUserId(final long userId);
}
