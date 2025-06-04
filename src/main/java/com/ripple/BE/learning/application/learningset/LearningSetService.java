package com.ripple.BE.learning.application.learningset;

import com.ripple.BE.learning.domain.learningset.LearningSet;
import com.ripple.BE.learning.domain.learningset.UserLearningSet;
import com.ripple.BE.learning.dto.response.learningset.UserLearningSetPreviewResponseDTO;
import com.ripple.BE.learning.exception.LearningException;
import com.ripple.BE.learning.exception.errorcode.LearningErrorCode;
import com.ripple.BE.learning.persistence.LearningSetRepository;
import com.ripple.BE.learning.persistence.UserLearningSetRepository;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.domain.type.Level;
import com.ripple.BE.user.exception.UserException;
import com.ripple.BE.user.exception.errorcode.UserErrorCode;
import com.ripple.BE.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class LearningSetService {

    private final LearningSetRepository learningSetRepository;
    private final UserLearningSetRepository userLearningSetRepository;
    private final UserRepository userRepository;

    /** 학습 세트 조회 */
    @Transactional(readOnly = true)
    public LearningSet findLearningSetById(final long learningSetId) {
        return learningSetRepository
                .findById(learningSetId)
                .orElseThrow(() -> new LearningException(LearningErrorCode.LEARNING_SET_NOT_FOUND));
    }

    /** 학습 세트 미리보기 목록 조회 */
    @Transactional
    public List<UserLearningSetPreviewResponseDTO> getLearningSetPreviewList(final long userId) {
        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        List<UserLearningSet> userLearningSetList =
                userLearningSetRepository.findByUserIdAndLevel(user.getId(), user.getCurrentLevel());

        if (userLearningSetList.isEmpty()) {
            List<UserLearningSet> created = createUserLearningSetsForAllLevels(user);
            userLearningSetList =
                    created.stream().filter(uls -> uls.getLevel() == user.getCurrentLevel()).toList();
        }

        return userLearningSetList.stream().map(UserLearningSetPreviewResponseDTO::from).toList();
    }

    /** 사용자의 학습 세트가 없을 경우 전체 레벨에 대해 새로 생성 */
    private List<UserLearningSet> createUserLearningSetsForAllLevels(final User user) {
        List<LearningSet> allLearningSetList = learningSetRepository.findAll();
        List<UserLearningSet> newUserLearningSetList = new ArrayList<>();

        for (Level level : Level.values()) {
            List<UserLearningSet> setsForLevel =
                    allLearningSetList.stream()
                            .map(
                                    learningSet ->
                                            UserLearningSet.withoutId(
                                                    user.getId(), learningSet.getId(), learningSet.getName(), level))
                            .toList();

            newUserLearningSetList.addAll(userLearningSetRepository.saveAll(setsForLevel));
        }

        return newUserLearningSetList;
    }
}
