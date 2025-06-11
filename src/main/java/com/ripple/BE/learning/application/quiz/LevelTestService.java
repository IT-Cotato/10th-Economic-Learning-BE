package com.ripple.BE.learning.application.quiz;

import static com.ripple.BE.user.exception.errorcode.UserErrorCode.*;

import com.ripple.BE.learning.application.quiz.command.SubmitLevelTestCommand;
import com.ripple.BE.learning.domain.quiz.Quiz;
import com.ripple.BE.learning.domain.type.Type;
import com.ripple.BE.learning.dto.response.leveltest.LevelTestAnswerResponseDTO;
import com.ripple.BE.learning.dto.response.leveltest.LevelTestQuizAnswerDTO;
import com.ripple.BE.learning.dto.response.leveltest.LevelTestQuizResponseDTO;
import com.ripple.BE.learning.dto.response.leveltest.LevelTestQuizStartResponseDTO;
import com.ripple.BE.learning.dto.response.leveltest.LevelTestResultResponseDTO;
import com.ripple.BE.learning.exception.LearningException;
import com.ripple.BE.learning.exception.errorcode.LearningErrorCode;
import com.ripple.BE.learning.persistence.QuizRepository;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.domain.type.Level;
import com.ripple.BE.user.exception.UserException;
import com.ripple.BE.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class LevelTestService {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final QuizSessionCacheManager quizSessionCacheManager;

    private static final Map<Type, Integer> SCORE_MAP =
            Map.of(
                    Type.OX, 1,
                    Type.MULTIPLE_CHOICE_SHORT, 2,
                    Type.MULTIPLE_CHOICE_LONG, 3);

    /** 레벨 테스트 퀴즈 목록 조회 */
    @Transactional
    public LevelTestQuizStartResponseDTO getLevelTestQuizList() {
        List<Quiz> allQuizzes = quizRepository.findAll();
        List<Quiz> selectedQuizzes = getShuffledQuizzesByLevel(allQuizzes);

        Map<Long, LevelTestQuizAnswerDTO> quizAnswerMap =
                selectedQuizzes.stream()
                        .collect(Collectors.toMap(Quiz::getId, LevelTestQuizAnswerDTO::from));

        // 레디스에 레벨 테스트 퀴즈 세션 저장
        String anonymousKey = UUID.randomUUID().toString(); // 익명 키 생성
        quizSessionCacheManager.storeLevelTestQuizSession(anonymousKey, quizAnswerMap);

        List<LevelTestQuizResponseDTO> quizResponses =
                selectedQuizzes.stream().map(LevelTestQuizResponseDTO::from).toList();
        return LevelTestQuizStartResponseDTO.of(anonymousKey, quizResponses);
    }

    /** 레벨 테스트 결과 제출 */
    @Transactional
    public LevelTestResultResponseDTO submitLevelTestResult(
            final SubmitLevelTestCommand quizSubmitCommand,
            final String levelTestKey,
            final Long userId) {

        Map<Long, LevelTestQuizAnswerDTO> quizAnswerMap =
                quizSessionCacheManager.getLevelTestQuizQuestions(levelTestKey);

        if (quizAnswerMap.isEmpty()) {
            throw new LearningException(LearningErrorCode.LEVEL_TEST_QUIZ_SESSION_EXPIRED);
        }

        int score = 0;
        int correctCount = 0;
        List<LevelTestAnswerResponseDTO> answerResults = new ArrayList<>();

        for (SubmitLevelTestCommand.AnswerCommand answer : quizSubmitCommand.answers()) {
            LevelTestQuizAnswerDTO expected = quizAnswerMap.get(answer.quizId());
            if (expected == null) {
                throw new LearningException(LearningErrorCode.LEVEL_TEST_QUIZ_NOT_FOUND);
            }
            boolean isCorrect = expected.answer().equals(answer.answer());

            if (isCorrect) {
                correctCount++;
                score += SCORE_MAP.getOrDefault(expected.type(), 0);
            }
            answerResults.add(LevelTestAnswerResponseDTO.of(expected, isCorrect));
        }

        Level level = assignUserLevel(userId, score);

        // 레벨 테스트 세션 완료 후 캐시 삭제
        quizSessionCacheManager.clearLevelTestSession(levelTestKey);

        return LevelTestResultResponseDTO.toQuizResponse(correctCount, level, answerResults);
    }

    private Level assignUserLevel(Long userId, int score) {
        User user =
                userRepository.findById(userId).orElseThrow(() -> new UserException(USER_NOT_FOUND));

        Level level = Level.fromScore(score);
        user.updateLevel(level);
        user.updateLevelTestCompleted(true);
        return level;
    }

    private List<Quiz> getShuffledQuizzesByLevel(List<Quiz> allQuizzes) {
        return Stream.of(Level.BEGINNER, Level.INTERMEDIATE, Level.ADVANCED)
                .flatMap(level -> pickRandomQuizzes(allQuizzes, level, 3).stream())
                .toList();
    }

    private List<Quiz> pickRandomQuizzes(List<Quiz> quizzes, Level level, int limit) {
        List<Quiz> filtered =
                quizzes.stream().filter(q -> q.getLevel() == level).collect(Collectors.toList());
        Collections.shuffle(filtered);
        return filtered.stream().limit(limit).toList();
    }
}
