package com.ripple.BE.learning.service.concept;

import com.ripple.BE.learning.domain.concept.Concept;
import com.ripple.BE.learning.domain.learningset.LearningSet;
import com.ripple.BE.learning.domain.learningset.UserLearningSet;
import com.ripple.BE.learning.dto.ConceptListDTO;
import com.ripple.BE.learning.exception.LearningException;
import com.ripple.BE.learning.exception.errorcode.LearningErrorCode;
import com.ripple.BE.learning.repository.ConceptRepository;
import com.ripple.BE.learning.repository.UserLearningSetRepository;
import com.ripple.BE.learning.service.learningset.LearningSetService;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.domain.type.Level;
import com.ripple.BE.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class ConceptService {

    private final LearningSetService learningSetService;
    private final UserService userService;

    private final UserLearningSetRepository userLearningSetRepository;
    private final ConceptRepository conceptRepository;

    /**
     * 학습 세트의 개념 목록을 조회
     *
     * @param learningSetId
     * @return 개념 목록
     */
    @Transactional(readOnly = true)
    public ConceptListDTO getConcepts(final long learningSetId, final Level level) {

        LearningSet learningSet = learningSetService.findLearningSetById(learningSetId);
        List<Concept> concepts = conceptRepository.findAllByLearningSetAndLevel(learningSet, level);

        return ConceptListDTO.toConceptListDTO(concepts);
    }

    /**
     * 개념 학습 완료 처리
     *
     * @param userId
     * @param learningSetId
     * @param level
     */
    @Transactional
    public void completeConceptLearning(
            final long userId, final long learningSetId, final Level level) {

        User user = userService.findUserById(userId);
        UserLearningSet userLearningSet =
                userLearningSetRepository
                        .findByUserIdAndLearningSetIdAndLevel(userId, learningSetId, level)
                        .orElseThrow(() -> new LearningException(LearningErrorCode.LEARNING_SET_NOT_FOUND));

        if (!userLearningSet.isConceptCompleted()) {
            userLearningSet.setConceptCompleted();
            userService.updateCompletedCountByLevel(user, level);
        }
    }
}
