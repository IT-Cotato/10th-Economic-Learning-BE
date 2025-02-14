package com.ripple.BE.learning.service.concept;

import com.ripple.BE.learning.domain.concept.Concept;
import com.ripple.BE.learning.domain.concept.ConceptScrap;
import com.ripple.BE.learning.domain.learningset.LearningSet;
import com.ripple.BE.learning.domain.learningset.UserLearningSet;
import com.ripple.BE.learning.dto.ConceptDTO;
import com.ripple.BE.learning.dto.ConceptListDTO;
import com.ripple.BE.learning.exception.LearningException;
import com.ripple.BE.learning.exception.errorcode.LearningErrorCode;
import com.ripple.BE.learning.repository.concept.ConceptRepository;
import com.ripple.BE.learning.repository.conceptScrap.ConceptScrapRepository;
import com.ripple.BE.learning.repository.learningSet.UserLearningSetRepository;
import com.ripple.BE.learning.service.learningset.LearningSetService;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.domain.type.Level;
import com.ripple.BE.user.service.AttendanceService;
import com.ripple.BE.user.service.UserProgressService;
import com.ripple.BE.user.service.UserService;
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

    private final LearningSetService learningSetService;
    private final UserService userService;
    private final UserProgressService userProgressService;
    private final AttendanceService attendanceService;

    private final UserLearningSetRepository userLearningSetRepository;
    private final ConceptRepository conceptRepository;
    private final ConceptScrapRepository conceptScrapRepository;

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

            userProgressService.updateLevel(user);
        }

        if (user.getCurrentLevel() == userLearningSet.getLevel()) {
            attendanceService.completeQuest(userId, "CONCEPT");
        }
    }

    /**
     * 개념 스크랩
     *
     * @param userId
     * @param conceptId
     */
    @Transactional
    public void scrapConcept(final long userId, final long conceptId) {
        // 이미 스크랩한 개념인지 확인
        if (conceptScrapRepository.existsByConcept_ConceptIdAndUserId(conceptId, userId)) {
            throw new LearningException(LearningErrorCode.CONCEPT_ALREADY_SCRAP);
        }

        User user = userService.findUserById(userId);
        Concept concept =
                conceptRepository
                        .findById(conceptId)
                        .orElseThrow(() -> new LearningException(LearningErrorCode.CONCEPT_NOT_FOUND));

        conceptScrapRepository.save(ConceptScrap.builder().user(user).concept(concept).build());
    }

    @Transactional
    public void removeScrapFromConcept(final long conceptId, final long userId) {
        ConceptScrap conceptScrap =
                conceptScrapRepository
                        .findByConcept_ConceptIdAndUserId(conceptId, userId)
                        .orElseThrow(() -> new LearningException(LearningErrorCode.CONCEPT_SCRAP_NOT_FOUND));

        conceptScrapRepository.delete(conceptScrap);
    }

    /**
     * 개별 개념 조회
     *
     * @param conceptId
     * @return 개념 반환
     */
    public ConceptDTO getConcept(final long conceptId) {
        Concept concept =
                conceptRepository
                        .findById(conceptId)
                        .orElseThrow(() -> new LearningException(LearningErrorCode.CONCEPT_NOT_FOUND));

        return ConceptDTO.toScrapConceptDTO(concept);
    }
}
