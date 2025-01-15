package com.ripple.BE.learning.service.learningset;

import com.ripple.BE.learning.domain.learningset.LearningSet;
import com.ripple.BE.learning.domain.learningset.UserLearningSet;
import com.ripple.BE.learning.dto.UserLearningSetListDTO;
import com.ripple.BE.learning.exception.LearningException;
import com.ripple.BE.learning.exception.errorcode.LearningErrorCode;
import com.ripple.BE.learning.repository.LearningSetRepository;
import com.ripple.BE.learning.repository.UserLearningSetRepository;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.domain.type.Level;
import com.ripple.BE.user.repository.UserRepository;
import com.ripple.BE.user.service.UserService;
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

    private final UserService userService;

    /**
     * 학습 세트 조회
     *
     * @param learningSetId
     * @return 학습 세트
     */
    @Transactional(readOnly = true)
    public LearningSet findLearningSetById(final long learningSetId) {
        return learningSetRepository
                .findById(learningSetId)
                .orElseThrow(() -> new LearningException(LearningErrorCode.LEARNING_SET_NOT_FOUND));
    }

    /**
     * 학습 세트 미리보기 목록 조회
     *
     * @param userId
     * @return 학습 세트 미리보기 목록
     */
    @Transactional
    public UserLearningSetListDTO getLearningSetPreviewList(final long userId) {

        User user = userService.findUserById(userId);
        List<UserLearningSet> userLearningSets =
                userLearningSetRepository.findByUserIdAndLevel(userId, user.getCurrentLevel());

        if (userLearningSets.isEmpty()) { // 사용자의 학습 세트가 없을 경우
            List<LearningSet> learningSets = learningSetRepository.findAll();
            for (Level level : Level.values()) {
                addUserLearningSet(user, learningSets, level);
                userRepository.flush();
            }

            userLearningSets =
                    userLearningSetRepository.findByUserIdAndLevel(userId, user.getCurrentLevel());
        }

        return UserLearningSetListDTO.toUserLearningSetListDTO(userLearningSets);
    }

    private void addUserLearningSet(User user, List<LearningSet> learningSetList, Level level) {

        List<UserLearningSet> userLearningSets =
                learningSetList.stream()
                        .map(learningSet -> UserLearningSet.toUserLearningSet(user, learningSet, level))
                        .toList();
        user.getUserLearningSetList().addAll(userLearningSets);
    }
}
