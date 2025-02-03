package com.ripple.BE.user.service;

import com.ripple.BE.learning.domain.learningset.UserLearningSet;
import com.ripple.BE.learning.repository.concept.ConceptRepository;
import com.ripple.BE.learning.repository.learningSet.UserLearningSetRepository;
import com.ripple.BE.learning.repository.quiz.QuizRepository;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.domain.type.Level;
import com.ripple.BE.user.dto.ProgressDTO;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserProgressService {

    private final UserService userService;

    private final UserLearningSetRepository userLearningSetRepository;
    private final ConceptRepository conceptRepository;
    private final QuizRepository quizRepository;

    @Transactional(readOnly = true)
    public ProgressDTO getLearningSetCompletionRate(final long userId) {

        User user = userService.findUserById(userId);

        // 레벨별 총 개념과 퀴즈 개수 계산
        Map<Level, Integer> totalSets =
                Arrays.stream(Level.values())
                        .collect(
                                Collectors.toMap(
                                        level -> level,
                                        level ->
                                                conceptRepository.countByLevel(level)
                                                        + quizRepository.countByLevel(level)));

        // 레벨별 완료율 계산
        return ProgressDTO.toProgressDTO(
                Arrays.stream(Level.values())
                        .collect(
                                Collectors.toMap(
                                        level -> level,
                                        level ->
                                                (double) user.getCompletedCountByLevel(level) / totalSets.get(level))));
    }

    @Transactional
    public void updateLevel(final User user) {
        List<UserLearningSet> userLearningSets =
                userLearningSetRepository.findByUserIdAndLevel(user.getId(), user.getCurrentLevel());

        if (!userLearningSets.isEmpty()) {
            if (userLearningSets.stream().allMatch(UserLearningSet::isLearningSetCompleted)) {
                if (user.getCurrentLevel() == Level.BEGINNER) {
                    user.updateLevel(Level.INTERMEDIATE);
                } else if (user.getCurrentLevel() == Level.INTERMEDIATE) {
                    user.updateLevel(Level.ADVANCED);
                }
            }
        }
    }
}
