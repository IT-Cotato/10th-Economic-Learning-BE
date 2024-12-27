package com.ripple.BE.learning.service;

import com.ripple.BE.learning.domain.LearningSet;
import com.ripple.BE.learning.domain.LearningSetComplete;
import com.ripple.BE.learning.dto.LearningSetCompleteListDTO;
import com.ripple.BE.learning.dto.ProgressDTO;
import com.ripple.BE.learning.exception.LearningException;
import com.ripple.BE.learning.exception.errorcode.LearningErrorCode;
import com.ripple.BE.learning.repository.LearningSetCompleteRepository;
import com.ripple.BE.learning.repository.LearningSetRepository;
import com.ripple.BE.user.domain.type.Level;
import com.ripple.BE.user.service.UserService;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class LearningSetService {

    private final UserService userService;
    private final LearningSetRepository learningSetRepository;
    private final LearningSetCompleteRepository learningSetCompleteRepository;

    /**
     * 학습 세트 조회
     *
     * @param learningSetId
     * @return 학습 세트
     */
    @Transactional(readOnly = true)
    protected LearningSet findLearningSetById(final long learningSetId) {
        return learningSetRepository
                .findById(learningSetId)
                .orElseThrow(() -> new LearningException(LearningErrorCode.LEARNING_SET_NOT_FOUND));
    }

    /**
     * 학습 세트 미리보기 목록 조회
     *
     * @param userId
     * @param level
     * @return 학습 세트 미리보기 목록
     */
    @Transactional(readOnly = true)
    public LearningSetCompleteListDTO getLearningSetPreviewList(
            final long userId, final Level level) {

        List<LearningSetComplete> completedLearningSets =
                learningSetCompleteRepository.findByUserIdAndLearningSetLevel(userId, level);

        return LearningSetCompleteListDTO.toLearningSetCompleteListDTO(completedLearningSets);
    }

    /**
     * 학습 세트 완료율 조회
     *
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public ProgressDTO getLearningSetCompletionRate(final long userId) {

        List<Long> completedLearningSetIds =
                learningSetCompleteRepository.findCompletedLearningSetIdsByUserId(userId);

        Set<Long> completedSetIdSet = new HashSet<>(completedLearningSetIds);

        Map<Level, Double> completionRates =
                Arrays.stream(Level.values())
                        .collect(
                                Collectors.toMap(
                                        level -> level,
                                        level -> calculateCompletionRate(level, completedSetIdSet))); // 최적화된 자료구조 활용

        return ProgressDTO.toProgressDTO(completionRates);
    }

    private double calculateCompletionRate(final Level level, final Set<Long> completedSetIdSet) {

        List<Long> learningSetIds = learningSetRepository.findIdsByLevel(level);

        if (learningSetIds.isEmpty()) {
            return 0.0;
        }

        long completedCount =
                learningSetIds.stream()
                        .filter(completedSetIdSet::contains) // 완료된 세트 ID만 필터링
                        .count();

        return (double) completedCount / learningSetIds.size() * 100;
    }
}
