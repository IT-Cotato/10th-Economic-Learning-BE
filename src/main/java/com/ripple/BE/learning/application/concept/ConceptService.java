package com.ripple.BE.learning.application.concept;

import com.ripple.BE.learning.domain.concept.Concept;
import com.ripple.BE.learning.domain.concept.ConceptScrap;
import com.ripple.BE.learning.domain.learningset.UserLearningSet;
import com.ripple.BE.learning.dto.response.concept.ConceptDetailResponseDTO;
import com.ripple.BE.learning.dto.response.concept.ConceptResponseDTO;
import com.ripple.BE.learning.exception.LearningException;
import com.ripple.BE.learning.exception.errorcode.LearningErrorCode;
import com.ripple.BE.learning.persistence.ConceptRepository;
import com.ripple.BE.learning.persistence.ConceptScrapRepository;
import com.ripple.BE.learning.persistence.UserLearningSetRepository;
import com.ripple.BE.user.domain.type.Level;
import com.ripple.BE.user.service.UserProgressManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class ConceptService {

    private final UserLearningSetRepository userLearningSetRepository;
    private final ConceptRepository conceptRepository;
    private final ConceptScrapRepository conceptScrapRepository;

    private final UserProgressManager userProgressManager;

    /** 학습 세트의 개념 목록을 조회 */
    public List<ConceptResponseDTO> getConcepts(final long learningSetId, final Level level) {

        List<Concept> concept = conceptRepository.findAllByLearningSetIdAndLevel(learningSetId, level);
        if (concept.isEmpty()) {
            throw new LearningException(LearningErrorCode.CONCEPT_NOT_FOUND);
        }

        return concept.stream().map(ConceptResponseDTO::from).toList();
    }

    /** 개념 학습 완료 처리 */
    @Transactional
    public void completeConceptLearning(
            final long userId, final long learningSetId, final Level level) {

        UserLearningSet userLearningSet =
                userLearningSetRepository
                        .findByUserIdAndLearningSetIdAndLevel(userId, learningSetId, level)
                        .orElseThrow(() -> new LearningException(LearningErrorCode.LEARNING_SET_NOT_FOUND));

        userProgressManager.updateAfterConcept(userId, userLearningSet);
    }

    /** 개념 스크랩 */
    @Transactional
    public void scrapConcept(final long userId, final long conceptId) {

        // 이미 스크랩한 개념인지 확인
        if (conceptScrapRepository.existsByUserIdAndConceptId(conceptId, userId)) {
            throw new LearningException(LearningErrorCode.CONCEPT_ALREADY_SCRAP);
        }
        if (!conceptRepository.existsById(conceptId)) {
            throw new LearningException(LearningErrorCode.CONCEPT_NOT_FOUND);
        }

        ConceptScrap conceptScrap = ConceptScrap.withoutId(userId, conceptId);
        conceptScrapRepository.save(conceptScrap);
    }

    @Transactional
    public void removeScrapFromConcept(final long conceptId, final long userId) {
        ConceptScrap conceptScrap =
                conceptScrapRepository
                        .findByUserIdAndConceptId(userId, conceptId)
                        .orElseThrow(() -> new LearningException(LearningErrorCode.CONCEPT_SCRAP_NOT_FOUND));

        conceptScrapRepository.delete(conceptScrap);
    }

    /** 개별 개념 조회 */
    public ConceptDetailResponseDTO getConcept(final long conceptId) {
        Concept concept =
                conceptRepository
                        .findById(conceptId)
                        .orElseThrow(() -> new LearningException(LearningErrorCode.CONCEPT_NOT_FOUND));

        return ConceptDetailResponseDTO.from(concept);
    }
}
