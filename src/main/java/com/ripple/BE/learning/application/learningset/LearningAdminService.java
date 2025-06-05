package com.ripple.BE.learning.application.learningset;

import com.ripple.BE.global.excel.ExcelUtils;
import com.ripple.BE.learning.domain.concept.Concept;
import com.ripple.BE.learning.domain.learningset.LearningSet;
import com.ripple.BE.learning.domain.learningset.LearningSetStat;
import com.ripple.BE.learning.domain.learningset.UserLearningSet;
import com.ripple.BE.learning.domain.quiz.Choice;
import com.ripple.BE.learning.domain.quiz.Quiz;
import com.ripple.BE.learning.dto.excel.ConceptExcelDTO;
import com.ripple.BE.learning.dto.excel.LearningSetExcelDTO;
import com.ripple.BE.learning.dto.excel.QuizExcelDTO;
import com.ripple.BE.learning.exception.LearningException;
import com.ripple.BE.learning.exception.errorcode.LearningErrorCode;
import com.ripple.BE.learning.persistence.ConceptRepository;
import com.ripple.BE.learning.persistence.LearningSetRepository;
import com.ripple.BE.learning.persistence.LearningSetStatRepository;
import com.ripple.BE.learning.persistence.QuizRepository;
import com.ripple.BE.learning.persistence.UserLearningSetRepository;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.domain.type.Level;
import com.ripple.BE.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.Arrays;
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

    private final UserLearningSetRepository userLearningSetRepository;
    private final LearningSetRepository learningSetRepository;
    private final LearningSetStatRepository learningSetStatRepository;
    private final QuizRepository quizJpaRepository;
    private final ConceptRepository conceptRepository;
    private final UserRepository userRepository;

    /** 엑셀 파일로부터 학습 세트를 생성 */
    @Transactional
    public void createLearningSetByExcel() {
        try {
            List<LearningSet> parsedLearningSets = parseLearningSetsFromExcel();
            List<LearningSet> existingLearningSets = learningSetRepository.findAll();

            Map<String, LearningSet> existingSetMap =
                    existingLearningSets.stream().collect(Collectors.toMap(LearningSet::getName, ls -> ls));

            // 이미 존재하는 학습 세트는 제외
            List<LearningSet> newLearningSets =
                    parsedLearningSets.stream()
                            .filter(ls -> !existingSetMap.containsKey(ls.getName()))
                            .toList();

            if (newLearningSets.isEmpty()) {
                return;
            }

            // 새로운 학습 세트 저장
            List<LearningSet> savedLearningSets = learningSetRepository.saveAll(newLearningSets);

            // 이름 기준으로 다시 Map 구성
            Map<String, LearningSet> savedSetMap =
                    savedLearningSets.stream().collect(Collectors.toMap(LearningSet::getName, ls -> ls));

            // 각 학습 세트에 개념 추가, 그리고 통계 업데이트
            List<Concept> conceptList = addConceptsToLearningSets(savedSetMap);
            updateConceptStatsByLevel(conceptList);

            // 각 학습 세트에 퀴즈 추가, 그리고 통계 업데이트
            List<Quiz> quizList = addQuizzesToLearningSets(savedSetMap);
            updateQuizStatsByLevel(quizList);

            // 사용자 학습 세트 추가
            addUserLearningSetsForNewLearningSets(savedLearningSets);

        } catch (Exception e) {
            log.error("Failed to save learning set by excel", e);
            throw new LearningException(LearningErrorCode.SAVE_LEARNING_SET_EXCEL_FILE_FAILED);
        }
    }

    /** 엑셀 파일에서 학습 세트 목록을 파싱 */
    private List<LearningSet> parseLearningSetsFromExcel() throws Exception {
        return ExcelUtils.parseExcelFile(FILE_PATH, LEARNING_SET_SHEET_INDEX).stream()
                .map(LearningSetExcelDTO::from)
                .map(LearningSetExcelDTO::learningSetName)
                .map(LearningSet::withoutId)
                .toList();
    }

    /** 엑셀 파일에서 학습 세트 이름을 기준으로 개념을 추가 */
    private List<Concept> addConceptsToLearningSets(Map<String, LearningSet> learningSetMap)
            throws Exception {
        List<Concept> conceptList = new ArrayList<>();

        ExcelUtils.parseExcelFile(FILE_PATH, CONCEPT_SHEET_INDEX).stream()
                .map(ConceptExcelDTO::from)
                .forEach(
                        dto -> {
                            LearningSet learningSet = learningSetMap.get(dto.learningSetName());

                            if (learningSet == null) return;

                            conceptList.add(
                                    Concept.withoutId(
                                            dto.level(),
                                            dto.name(),
                                            dto.explanation(),
                                            learningSet.getId(),
                                            learningSet.getName()));
                        });

        return conceptRepository.saveAll(conceptList);
    }

    /** 엑셀 파일에서 학습 세트 이름을 기준으로 퀴즈를 추가 */
    private List<Quiz> addQuizzesToLearningSets(Map<String, LearningSet> learningSetMap)
            throws Exception {
        List<Quiz> quizList = new ArrayList<>();

        ExcelUtils.parseExcelFile(LearningAdminService.FILE_PATH, QUIZ_SHEET_INDEX).stream()
                .map(QuizExcelDTO::from)
                .forEach(
                        excel -> {
                            LearningSet learningSet = learningSetMap.get(excel.learningSetName());

                            if (learningSet == null) return;

                            quizList.add(
                                    Quiz.withoutId(
                                            excel.level(),
                                            excel.type(),
                                            excel.name(),
                                            excel.question(),
                                            excel.answer(),
                                            excel.explanation(),
                                            learningSet.getId(),
                                            learningSet.getName(),
                                            excel.choices().stream()
                                                    .map(choice -> Choice.withoutId(choice.content()))
                                                    .toList()));
                        });
        return quizJpaRepository.saveAll(quizList);
    }

    /** 레벨별 개념 통계 업데이트 */
    private void updateConceptStatsByLevel(List<Concept> concepts) {
        Map<Level, Long> counts =
                concepts.stream().collect(Collectors.groupingBy(Concept::getLevel, Collectors.counting()));

        counts.forEach(
                (level, count) -> {
                    LearningSetStat stat =
                            learningSetStatRepository
                                    .findByLevel(level)
                                    .orElseGet(() -> new LearningSetStat(level, 0, 0));
                    stat.addConcepts(count.intValue());
                    learningSetStatRepository.save(stat);
                });
    }

    /** 레벨별 퀴즈 통계 업데이트 */
    private void updateQuizStatsByLevel(List<Quiz> quizzes) {
        Map<Level, Long> counts =
                quizzes.stream().collect(Collectors.groupingBy(Quiz::getLevel, Collectors.counting()));

        counts.forEach(
                (level, count) -> {
                    LearningSetStat stat =
                            learningSetStatRepository
                                    .findByLevel(level)
                                    .orElseGet(() -> new LearningSetStat(level, 0, 0));
                    stat.addQuizzes(count.intValue());
                    learningSetStatRepository.save(stat);
                });
    }

    /** 새 학습 세트에 대한 사용자 학습 세트 추가 */
    private void addUserLearningSetsForNewLearningSets(List<LearningSet> newLearningSets) {
        log.info("새 학습 세트에 대한 사용자 학습 세트 추가 시작...");

        List<User> users = userRepository.findAll();

        List<UserLearningSet> userLearningSetsToSave =
                users.stream()
                        .flatMap(
                                user ->
                                        newLearningSets.stream()
                                                .flatMap(
                                                        learningSet ->
                                                                Arrays.stream(Level.values())
                                                                        .map(
                                                                                level ->
                                                                                        UserLearningSet.withoutId(
                                                                                                user.getId(),
                                                                                                learningSet.getId(),
                                                                                                learningSet.getName(),
                                                                                                level))))
                        .toList();

        if (!userLearningSetsToSave.isEmpty()) {
            userLearningSetRepository.saveAll(userLearningSetsToSave);
        }

        log.info("사용자 학습 세트 추가 완료.");
    }
}
