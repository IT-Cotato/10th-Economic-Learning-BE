package com.ripple.BE.learning.service;

import static com.ripple.BE.learning.exception.errorcode.QuizErrorCode.*;
import static com.ripple.BE.user.exception.errorcode.UserErrorCode.*;

import com.ripple.BE.learning.domain.Quiz;
import com.ripple.BE.learning.domain.type.Purpose;
import com.ripple.BE.learning.domain.type.Type;
import com.ripple.BE.learning.dto.QuizDTO;
import com.ripple.BE.learning.dto.QuizListDTO;
import com.ripple.BE.learning.dto.QuizSubmitDTO;
import com.ripple.BE.learning.dto.response.LevelTestResultResponse;
import com.ripple.BE.learning.exception.QuizException;
import com.ripple.BE.learning.repository.QuizRepository;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.domain.type.Level;
import com.ripple.BE.user.exception.UserException;
import com.ripple.BE.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

    private static final int BEGINNER_SCORE = 6;
    private static final int INTERMEDIATE_SCORE = 12;

    @Transactional
    public void addLevelTestQuiz(QuizDTO quizDTO) {
        quizRepository.save(Quiz.toQuiz(quizDTO));
    }

    /** 레벨 테스트 퀴즈 목록 조회 */
    public QuizListDTO getLevelTestQuizList() {
        List<Quiz> list = quizRepository.findAllByPurpose(Purpose.LEVEL_TEST);

        return QuizListDTO.toQuizListDTO(list);
        // 추후 학습, 용어 사전 데이터 추가 후 4지선다 답안 랜덤으로 넣는 로직 추가
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
        List<LevelTestResultResponse.Explanation> wrongAnswers = new ArrayList<>();

        for (QuizSubmitDTO.Answer answer : quizSubmitDTO.answers()) {
            Quiz quiz = quizMap.get(answer.quizId());
            if (quiz == null) {
                throw new QuizException(QUIZ_NOT_FOUND);
            }

            if (quiz.getAnswer().equals(answer.answer())) {
                correctCount++;
                score += scoreMap.get(quiz.getType());
            } else {
                wrongAnswers.add(LevelTestResultResponse.Explanation.from(quiz));
            }
        }

        Level level = updateLevel(userId, score);

        return LevelTestResultResponse.from(correctCount, level, wrongAnswers);
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
}
