package com.ripple.BE.learning.service.learningset;

import com.ripple.BE.global.excel.ExcelUtils;
import com.ripple.BE.learning.domain.concept.Concept;
import com.ripple.BE.learning.domain.learningset.LearningSet;
import com.ripple.BE.learning.domain.quiz.Choice;
import com.ripple.BE.learning.domain.quiz.Quiz;
import com.ripple.BE.learning.dto.ConceptDTO;
import com.ripple.BE.learning.dto.LearningSetDTO;
import com.ripple.BE.learning.dto.QuizDTO;
import com.ripple.BE.learning.exception.LearningException;
import com.ripple.BE.learning.exception.errorcode.LearningErrorCode;
import com.ripple.BE.learning.repository.LearningSetRepository;
import com.ripple.BE.learning.repository.QuizRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LearningAdminService {

    private static final String FILE_PATH = "static/excel/example.xlsx";
    private static final int LEARNING_SET_SHEET_INDEX = 0;
    private static final int CONCEPT_SHEET_INDEX = 1;
    private static final int QUIZ_SHEET_INDEX = 2;

    private final LearningSetRepository learningSetRepository;
    private final QuizRepository quizRepository;

    /** 엑셀 파일로부터 학습 세트를 생성 */
    @Transactional
    public void createLearningSetByExcel() {
        try {

            List<LearningSet> learningSetList = parseLearningSetsFromExcel();
            List<LearningSet> existingLearningSets = learningSetRepository.findAll();

            Map<String, LearningSet> learningSetMap =
                    learningSetList.stream()
                            .collect(Collectors.toMap(LearningSet::getName, learningSet -> learningSet));

            Map<String, LearningSet> existingLearningSetMap =
                    existingLearningSets.stream()
                            .collect(Collectors.toMap(LearningSet::getName, learningSet -> learningSet));

            // 새로운 학습 세트만 필터링
            Map<String, LearningSet> newLearningSetMap =
                    learningSetMap.entrySet().stream()
                            .filter(entry -> !existingLearningSetMap.containsKey(entry.getKey()))
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            List<LearningSet> newLearningSets =
                    learningSetList.stream()
                            .filter(
                                    learningSet ->
                                            existingLearningSets.stream()
                                                    .noneMatch(
                                                            existingSet -> existingSet.getName().equals(learningSet.getName())))
                            .collect(Collectors.toList());

            addConceptsToLearningSets(FILE_PATH, newLearningSetMap);
            addQuizzesToLearningSets(FILE_PATH, newLearningSetMap);

            learningSetRepository.saveAll(newLearningSets);

        } catch (Exception e) {
            log.error("Failed to save learning set by excel", e);
            throw new LearningException(LearningErrorCode.SAVE_LEARNING_SET_EXCEL_FILE_FAILED);
        }
    }

    private List<LearningSet> parseLearningSetsFromExcel() throws Exception {
        return ExcelUtils.parseExcelFile(LearningAdminService.FILE_PATH, LEARNING_SET_SHEET_INDEX)
                .stream()
                .map(LearningSetDTO::toLearningSetDTO)
                .map(LearningSet::toLearningSet)
                .collect(Collectors.toList());
    }

    private void addConceptsToLearningSets(String filePath, Map<String, LearningSet> learningSetMap)
            throws Exception {
        ExcelUtils.parseExcelFile(filePath, CONCEPT_SHEET_INDEX).stream()
                .map(ConceptDTO::toConceptDTO)
                .forEach(
                        conceptDTO -> {
                            LearningSet learningSet = learningSetMap.get(conceptDTO.learningSetName());

                            if (learningSet == null) {
                                return;
                            }

                            Concept concept = Concept.toConcept(conceptDTO);
                            concept.setLearningSet(learningSet);
                        });
    }

    private void addQuizzesToLearningSets(String filePath, Map<String, LearningSet> learningSetMap)
            throws Exception {
        ExcelUtils.parseExcelFile(filePath, QUIZ_SHEET_INDEX).stream()
                .map(QuizDTO::toQuizDTO)
                .forEach(
                        quizDTO -> {
                            LearningSet learningSet = learningSetMap.get(quizDTO.learningSetName());

                            if (learningSet == null) {
                                return; // 해당 quizDTO를 건너뜀
                            }

                            Quiz quiz = Quiz.toQuiz(quizDTO);
                            quiz.setLearningSet(learningSet);

                            quizRepository.save(quiz);

                            quizDTO
                                    .choiceList()
                                    .choices()
                                    .forEach(
                                            choiceDTO -> {
                                                Choice choice = Choice.toChoice(choiceDTO);
                                                choice.setQuiz(quiz);
                                            });
                        });
    }
}
