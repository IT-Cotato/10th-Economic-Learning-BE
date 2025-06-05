package com.ripple.BE.learning.persistence.impl;

import com.ripple.BE.learning.domain.quiz.Quiz;
import com.ripple.BE.learning.persistence.QuizRepository;
import com.ripple.BE.learning.persistence.jpa.entity.quiz.QuizJpaEntity;
import com.ripple.BE.learning.persistence.jpa.repository.quiz.QuizJpaRepository;
import com.ripple.BE.user.domain.type.Level;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuizRepositoryImpl implements QuizRepository {

    private final QuizJpaRepository quizJpaRepository;

    @Override
    public List<Quiz> findAllByLearningSetIdAndLevel(final long learningSetId, final Level level) {
        return quizJpaRepository.findAllByLearningSetIdAndLevel(learningSetId, level).stream()
                .map(QuizJpaEntity::toModel)
                .toList();
    }

    @Override
    public Optional<Quiz> findById(final long quizId) {
        return quizJpaRepository.findById(quizId).map(QuizJpaEntity::toModel);
    }

    @Override
    public List<Quiz> findAll() {
        return quizJpaRepository.findAll().stream().map(QuizJpaEntity::toModel).toList();
    }

    @Override
    public List<Quiz> findFailedQuizzesByUserAndLevel(final long userId, final Level level) {
        return quizJpaRepository.findFailedQuizzesByUserIdAndLevel(userId, level).stream()
                .map(QuizJpaEntity::toModel)
                .toList();
    }

    @Override
    public List<Quiz> saveAll(final List<Quiz> quizzes) {
        List<QuizJpaEntity> quizEntities = quizzes.stream().map(QuizJpaEntity::from).toList();

        return quizJpaRepository.saveAll(quizEntities).stream().map(QuizJpaEntity::toModel).toList();
    }

    @Override
    public boolean existsById(final long quizId) {
        return quizJpaRepository.existsById(quizId);
    }
}
