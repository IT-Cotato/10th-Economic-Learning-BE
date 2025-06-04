package com.ripple.BE.user.service;

import com.ripple.BE.learning.persistence.jpa.entity.learningset.UserLearningSetJpaEntity;
import com.ripple.BE.learning.persistence.jpa.repository.learningset.UserLearningSetJpaRepository;
import com.ripple.BE.user.domain.type.Level;
import com.ripple.BE.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class UserStatService {

    private final UserLearningSetJpaRepository userLearningSetJpaRepository;
    private final UserRepository userRepository;

    public void updateQuizStats(
            final long userId, final Level level, final int quizCount, final int correctCount) {
        userRepository.addQuizStats(userId, quizCount, correctCount);
        userRepository.incrementCompletedCount(userId, level.name());
    }

    public void updateConceptStats(final long userId, final Level level) {
        userRepository.incrementCompletedCount(userId, level.name());
    }

    public void tryUpgradeLevel(final long userId, final Level currentLevel) {
        boolean allCompleted =
                userLearningSetJpaRepository.findByUserIdAndLevel(userId, currentLevel).stream()
                        .allMatch(UserLearningSetJpaEntity::isLearningSetCompleted);

        if (allCompleted) {
            currentLevel.nextLevel().ifPresent(next -> userRepository.updateLevel(userId, next.name()));
        }
    }
}
