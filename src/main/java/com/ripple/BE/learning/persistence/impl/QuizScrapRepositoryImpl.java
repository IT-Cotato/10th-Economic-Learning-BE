package com.ripple.BE.learning.persistence.impl;

import com.ripple.BE.learning.domain.quiz.Quiz;
import com.ripple.BE.learning.domain.quiz.QuizScrap;
import com.ripple.BE.learning.persistence.QuizScrapRepository;
import com.ripple.BE.learning.persistence.jpa.entity.quiz.QuizJpaEntity;
import com.ripple.BE.learning.persistence.jpa.entity.quiz.QuizScrapJpaEntity;
import com.ripple.BE.learning.persistence.jpa.repository.quizscrap.QuizScrapJpaRepository;
import com.ripple.BE.user.domain.type.Level;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuizScrapRepositoryImpl implements QuizScrapRepository {

    private final QuizScrapJpaRepository quizScrapJpaRepository;

    @Override
    public Optional<QuizScrap> findByQuizIdAndUserId(final long quizId, final long userId) {
        return quizScrapJpaRepository
                .findByQuizIdAndUserId(quizId, userId)
                .map(QuizScrapJpaEntity::toModel);
    }

    @Override
    public void delete(final QuizScrap quizScrap) {
        quizScrapJpaRepository.deleteByQuizIdAndUserId(quizScrap.getQuizId(), quizScrap.getUserId());
    }

    @Override
    public boolean existsByQuizIdAndUserId(final long quizId, final long userId) {
        return quizScrapJpaRepository.existsByQuizIdAndUserId(quizId, userId);
    }

    @Override
    public QuizScrap save(final QuizScrap quizScrap) {
        return quizScrapJpaRepository.save(QuizScrapJpaEntity.from(quizScrap)).toModel();
    }

    @Override
    public List<Quiz> findQuizScrappedByUserIdAndLevel(Long userId, Level level) {
        return quizScrapJpaRepository.findQuizScrappedByUserIdAndLevel(userId, level).stream()
                .map(QuizJpaEntity::toModel)
                .toList();
    }

    @Override
    public void deleteAllByUserId(final long userId) {
        quizScrapJpaRepository.deleteAllByUserId(userId);
    }
}
