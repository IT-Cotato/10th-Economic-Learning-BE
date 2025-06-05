package com.ripple.BE.user.service;

import com.ripple.BE.learning.domain.learningset.LearningSetStat;
import com.ripple.BE.learning.persistence.LearningSetStatRepository;
import com.ripple.BE.learning.persistence.jpa.entity.learningset.UserLearningSetJpaEntity;
import com.ripple.BE.learning.persistence.jpa.repository.learningset.UserLearningSetJpaRepository;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.domain.type.Level;
import com.ripple.BE.user.dto.response.UserCompletionRateByLevelDTOResponse;
import com.ripple.BE.user.exception.UserException;
import com.ripple.BE.user.exception.errorcode.UserErrorCode;
import com.ripple.BE.user.repository.UserRepository;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class UserStatService {

    private final UserLearningSetJpaRepository userLearningSetJpaRepository;
    private final LearningSetStatRepository learningSetStatRepository;
    private final UserRepository userRepository;

    /** 퀴즈 통계 업데이트 */
    public void updateQuizStats(
            final long userId, final Level level, final int quizCount, final int correctCount) {
        userRepository.addQuizStats(userId, quizCount, correctCount);
        userRepository.incrementCompletedCount(userId, level.name());
    }

    /** 개념 통계 업데이트 */
    public void updateConceptStats(final long userId, final Level level) {
        userRepository.incrementCompletedCount(userId, level.name());
    }

    /** 사용자 레벨 업그레이드 시도 */
    public void tryUpgradeLevel(final long userId, final Level currentLevel) {
        boolean allCompleted =
                userLearningSetJpaRepository.findByUserIdAndLevel(userId, currentLevel).stream()
                        .allMatch(UserLearningSetJpaEntity::isLearningSetCompleted);

        if (allCompleted) {
            currentLevel.nextLevel().ifPresent(next -> userRepository.updateLevel(userId, next.name()));
        }
    }

    /** 사용자 레벨별 학습 세트 완료율 조회 */
    @Transactional(readOnly = true)
    public UserCompletionRateByLevelDTOResponse getLearningSetCompletionRate(final long userId) {
        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        // 통계 테이블에서 레벨별 총 개수 가져오기
        Map<Level, Integer> totalSets =
                learningSetStatRepository.findAll().stream()
                        .collect(
                                Collectors.toMap(
                                        LearningSetStat::getLevel,
                                        LearningSetStat::getTotalCount,
                                        (oldVal, newVal) -> oldVal,
                                        () -> new EnumMap<>(Level.class)));

        // 완료율 계산
        Map<Level, Double> completionRate =
                totalSets.entrySet().stream()
                        .collect(
                                Collectors.toMap(
                                        Map.Entry::getKey,
                                        entry -> {
                                            long completed = user.getCompletedCountByLevel(entry.getKey());
                                            int total = entry.getValue();
                                            return total == 0 ? 0.0 : ((double) completed / total) * 100;
                                        },
                                        (oldVal, newVal) -> oldVal,
                                        () -> new EnumMap<>(Level.class)));

        return UserCompletionRateByLevelDTOResponse.from(completionRate);
    }
}
