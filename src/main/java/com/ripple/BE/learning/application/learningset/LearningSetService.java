package com.ripple.BE.learning.application.learningset;

import com.ripple.BE.learning.application.learningset.event.UserCreatedEvent;
import com.ripple.BE.learning.domain.learningset.LearningSet;
import com.ripple.BE.learning.domain.learningset.UserLearningSet;
import com.ripple.BE.learning.dto.response.learningset.UserLearningSetPreviewResponseDTO;
import com.ripple.BE.learning.persistence.LearningSetRepository;
import com.ripple.BE.learning.persistence.UserLearningSetRepository;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.domain.type.Level;
import com.ripple.BE.user.exception.UserException;
import com.ripple.BE.user.exception.errorcode.UserErrorCode;
import com.ripple.BE.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class LearningSetService {

    private final LearningSetRepository learningSetRepository;
    private final UserLearningSetRepository userLearningSetRepository;
    private final UserRepository userRepository;

    /** 학습 세트 미리보기 목록 조회 */
    @Transactional
    public List<UserLearningSetPreviewResponseDTO> getLearningSetPreviewList(final long userId) {
        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        List<UserLearningSet> userLearningSetList =
                userLearningSetRepository.findByUserIdAndLevel(user.getId(), user.getCurrentLevel());

        return userLearningSetList.stream().map(UserLearningSetPreviewResponseDTO::from).toList();
    }

    /** 사용자의 학습 세트가 없을 경우 전체 레벨에 대해 새로 생성 */
    @EventListener
    @Transactional
    public void createUserLearningSetsForAllLevels(final UserCreatedEvent event) {
        List<LearningSet> allLearningSetList = learningSetRepository.findAll();
        User user = event.user();

        for (Level level : Level.values()) {
            List<UserLearningSet> setsForLevel =
                    allLearningSetList.stream()
                            .map(
                                    learningSet ->
                                            UserLearningSet.withoutId(
                                                    user.getId(), learningSet.getId(), learningSet.getName(), level))
                            .toList();

            userLearningSetRepository.saveAll(setsForLevel);
        }
    }
}
