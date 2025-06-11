package com.ripple.BE.learning.persistence.impl;

import com.ripple.BE.learning.domain.quiz.FailQuiz;
import com.ripple.BE.learning.persistence.FailQuizRepository;
import com.ripple.BE.learning.persistence.jpa.entity.quiz.FailQuizJpaEntity;
import com.ripple.BE.learning.persistence.jpa.repository.failquiz.FailQuizJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FailQuizRepositoryImpl implements FailQuizRepository {

    private final FailQuizJpaRepository failQuizJpaRepository;

    @Override
    public void saveAll(final List<FailQuiz> failQuizzes) {
        if (failQuizzes.isEmpty()) {
            return;
        }
        failQuizJpaRepository.saveAll(failQuizzes.stream().map(FailQuizJpaEntity::from).toList());
    }
}
