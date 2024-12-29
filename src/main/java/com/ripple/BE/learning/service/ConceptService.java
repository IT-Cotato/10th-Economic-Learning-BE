package com.ripple.BE.learning.service;

import com.ripple.BE.learning.domain.LearningSetComplete;
import com.ripple.BE.learning.dto.ConceptListDTO;
import com.ripple.BE.learning.exception.LearningException;
import com.ripple.BE.learning.exception.errorcode.LearningErrorCode;
import com.ripple.BE.learning.repository.LearningSetCompleteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class ConceptService {

    private final LearningSetService learningSetService;

    private final LearningSetCompleteRepository learningSetCompleteRepository;

    /**
     * 학습 세트의 개념 목록을 조회
     *
     * @param learningSetId
     * @return 개념 목록
     */
    @Transactional(readOnly = true)
    public ConceptListDTO getConcepts(final long learningSetId) {

        return ConceptListDTO.toConceptListDTO(
                learningSetService.findLearningSetById(learningSetId).getConcepts());
    }

    /**
     * 개념 학습 완료 처리
     *
     * @param userId
     * @param learningSetId
     */
    @Transactional
    public void completeConceptLearning(final long userId, final long learningSetId) {

        LearningSetComplete learningSetComplete =
                learningSetCompleteRepository
                        .findByUserIdAndLearningSetId(userId, learningSetId)
                        .orElseThrow(() -> new LearningException(LearningErrorCode.LEARNING_SET_NOT_FOUND));

        // 학습 완료 처리
        learningSetComplete.setConceptCompleted(true);
    }
}
