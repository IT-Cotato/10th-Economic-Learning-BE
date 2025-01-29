package com.ripple.BE.learning.service;

import static com.ripple.BE.learning.exception.errorcode.QuizErrorCode.*;
import static com.ripple.BE.learning.service.quiz.QuizRedisService.*;
import static com.ripple.BE.user.exception.errorcode.UserErrorCode.*;

import com.ripple.BE.learning.domain.quiz.Quiz;
import com.ripple.BE.learning.domain.type.Purpose;
import com.ripple.BE.learning.domain.type.Type;
import com.ripple.BE.learning.dto.AnswerDTO;
import com.ripple.BE.learning.dto.QuizDTO;
import com.ripple.BE.learning.dto.QuizListDTO;
import com.ripple.BE.learning.dto.QuizSubmitDTO;
import com.ripple.BE.learning.dto.response.LevelTestResultResponse;
import com.ripple.BE.learning.exception.QuizException;
import com.ripple.BE.learning.repository.quiz.QuizRepository;
import com.ripple.BE.learning.service.quiz.QuizRedisService;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.domain.type.Level;
import com.ripple.BE.user.exception.UserException;
import com.ripple.BE.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
@Slf4j
public class LevelTestService {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final QuizRedisService quizRedisService;

    private static final int BEGINNER_SCORE = 6;
    private static final int INTERMEDIATE_SCORE = 12;

    private static final String QUESTION_TYPE = "questions";
    private static final String QUIZ_COUNT = "quizCount";

    @Transactional
    public void addLevelTestQuiz(QuizDTO quizDTO) {
        quizRepository.save(Quiz.toQuiz(quizDTO));
    }

    /** 레벨 테스트 퀴즈 목록 조회 */
    public QuizListDTO getLevelTestQuizList(Long sessionId) {
        List<Quiz> quizList = quizRepository.findAll();

        // 레벨별 퀴즈 목록 조회
        List<Quiz> beginnerQuizzes = getRandomQuizzes(quizList, Level.BEGINNER);
        List<Quiz> intermediateQuizzes = getRandomQuizzes(quizList, Level.INTERMEDIATE);
        List<Quiz> advancedQuizzes = getRandomQuizzes(quizList, Level.ADVANCED);

        // 전체 레벨 테스트 퀴즈 목록 생성
        List<Quiz> finalQuizzes =
                Stream.concat(
                                Stream.concat(beginnerQuizzes.stream(), intermediateQuizzes.stream()),
                                advancedQuizzes.stream())
                        .collect(Collectors.toList());

        QuizListDTO quizListDTO = QuizListDTO.toQuizListDTO(finalQuizzes);

        quizRedisService.saveToRedis(sessionId, QUESTION_TYPE, quizListDTO);
        quizRedisService.saveToRedis(sessionId, QUIZ_COUNT, quizList.size());

        return quizListDTO;
    }

    /**
     * 레벨 테스트 결과 제출
     *
     * @param quizSubmitDTO
     * @param userId
     * @return LevelTestResultResponse
     */
    @Transactional
    public LevelTestResultResponse submitLevelTestResult(QuizSubmitDTO quizSubmitDTO, Long userId) {
        // 레벨 테스트 퀴즈 목록 조회
        Map<Long, Quiz> quizMap =
                quizRepository.findAllByPurpose(Purpose.LEVEL_TEST).stream()
                        .collect(Collectors.toMap(Quiz::getId, quiz -> quiz));

        Map<Type, Integer> scoreMap =
                Map.of(
                        Type.OX, 1,
                        Type.MULTIPLE_CHOICE_SHORT, 2,
                        Type.MULTIPLE_CHOICE_LONG, 3);

        int correctCount = 0;
        int score = 0;

        List<AnswerDTO> wrongAnswers = new ArrayList<>();

        for (QuizSubmitDTO.Answer answer : quizSubmitDTO.answers()) {
            Quiz quiz = quizMap.get(answer.quizId());
            if (quiz == null) {
                throw new QuizException(QUIZ_NOT_FOUND);
            }

            if (quiz.getAnswer().equals(answer.answer())) {
                correctCount++;
                score += scoreMap.get(quiz.getType());
            } else {
                wrongAnswers.add(AnswerDTO.toanswerDTO(quiz));
            }
        }

        Level level = updateLevel(userId, score);

        return LevelTestResultResponse.toQuizResponse(correctCount, level, wrongAnswers);
    }

    /**
     * 레벨 테스트 결과에 따른 레벨 업데이트
     *
     * @param userId
     * @param score
     * @return
     */
    private Level updateLevel(Long userId, int score) {
        User user =
                userRepository.findById(userId).orElseThrow(() -> new UserException(USER_NOT_FOUND));
        Level level = calculateLevel(score);
        user.updateLevel(level);
        return level;
    }

    /**
     * 레벨 테스트 결과에 따른 레벨 계산
     *
     * @param score
     * @return
     */
    private Level calculateLevel(int score) {
        if (score <= BEGINNER_SCORE) {
            return Level.BEGINNER;
        } else if (score <= INTERMEDIATE_SCORE) {
            return Level.INTERMEDIATE;
        } else {
            return Level.ADVANCED;
        }
    }

    /**
     * 레벨 테스트 퀴즈 랜덤 조회
     *
     * @param quizList
     * @param level
     * @return
     */
    private List<Quiz> getRandomQuizzes(List<Quiz> quizList, Level level) {
        List<Quiz> quizzes =
                quizList.stream().filter(quiz -> quiz.getLevel() == level).collect(Collectors.toList());
        Collections.shuffle(quizzes);
        return quizzes.stream().limit(3).toList();
    }
}
