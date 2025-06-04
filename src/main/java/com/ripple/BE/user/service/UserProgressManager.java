package com.ripple.BE.user.service;

import com.ripple.BE.learning.domain.learningset.UserLearningSet;
import com.ripple.BE.learning.domain.quiz.FailQuiz;
import com.ripple.BE.learning.domain.quiz.Quiz;
import com.ripple.BE.learning.exception.LearningException;
import com.ripple.BE.learning.exception.errorcode.LearningErrorCode;
import com.ripple.BE.learning.persistence.FailQuizRepository;
import com.ripple.BE.learning.persistence.UserLearningSetRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserProgressManager {

    private final UserLearningSetRepository userLearningSetRepository;
    private final FailQuizRepository failQuizRepository;

    private final AttendanceService attendanceService;
    private final UserService userService;
    private final UserStatService userStatService;

    public void updateAfterQuiz(
            final long userId, final Quiz quiz, final List<FailQuiz> failQuizList, final int total) {

        UserLearningSet userLearningSet =
                userLearningSetRepository
                        .findByUserIdAndLearningSetIdAndLevel(userId, quiz.getLearningSetId(), quiz.getLevel())
                        .orElseThrow(() -> new LearningException(LearningErrorCode.LEARNING_SET_NOT_FOUND));

        // 이미 퀴즈를 완료한 경우, 중복 처리 방지
        if (userLearningSet.isQuizCompleted()) {
            return;
        }

        int correct = total - failQuizList.size();

        // 퀴즈 완료 상태 업데이트
        userLearningSetRepository.save(userLearningSet.updateQuizCompleted());

        // 틀린 퀴즈 저장
        failQuizRepository.saveAll(failQuizList);

        // 사용자 통계 업데이트, 푼 문제 수, 정답 수
        userStatService.updateQuizStats(userId, quiz.getLevel(), total, correct);

        // 사용자 레벨 업그레이드 시도
        userStatService.tryUpgradeLevel(userId, quiz.getLevel());

        // 출석 퀘스트 완료
        if (isSameLevel(userId, userLearningSet)) {
            attendanceService.completeQuest(userId, "QUIZ");
        }
    }

    public void updateAfterConcept(final long userId, final UserLearningSet set) {

        // 이미 개념 학습을 완료한 경우, 중복 처리 방지
        if (set.isConceptCompleted()) return;

        // 개념 학습 완료 상태 업데이트
        userLearningSetRepository.save(set.updateConceptCompleted());

        // 사용자 통계 업데이트
        userStatService.updateConceptStats(userId, set.getLevel());

        // 사용자 레벨 업그레이드 시도
        userStatService.tryUpgradeLevel(userId, set.getLevel());

        // 출석 퀘스트 완료
        if (isSameLevel(userId, set)) {
            attendanceService.completeQuest(userId, "CONCEPT");
        }
    }

    // 사용자 레벨과 학습 세트 레벨이 동일한지 확
    private boolean isSameLevel(Long userId, UserLearningSet set) {
        return set.getLevel() == userService.findUserById(userId).getCurrentLevel();
    }
}
