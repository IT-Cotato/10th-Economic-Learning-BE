package com.ripple.BE.learning.service;

import static com.ripple.BE.learning.exception.errorcode.QuizErrorCode.*;
import static com.ripple.BE.user.exception.errorcode.UserErrorCode.*;

import com.ripple.BE.learning.domain.Quiz;
import com.ripple.BE.learning.domain.type.Purpose;
import com.ripple.BE.learning.domain.type.Type;
import com.ripple.BE.learning.dto.QuizDTO;
import com.ripple.BE.learning.dto.request.SubmitLevelTestRequest;
import com.ripple.BE.learning.dto.response.LevelTestQuizListResponse;
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

    @Transactional
    public void addLevelTestQuiz(QuizDTO quizDTO) {
        quizRepository.save(Quiz.toQuiz(quizDTO));
    }

    public List<LevelTestQuizListResponse> getLevelTestQuizList() {
        List<LevelTestQuizListResponse> list =
                quizRepository.findAll().stream().map(LevelTestQuizListResponse::from).toList();

        // 추후 학습, 용어 사전 데이터 추가 후 4지선다 답안 랜덤으로 넣는 로직 추가
        return list;
    }

    @Transactional
    public LevelTestResultResponse submitLevelTestResult(
            SubmitLevelTestRequest request, Long userId) {
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

        // 정답 확인
        for (SubmitLevelTestRequest.Answer answer : request.answers()) {
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

        // 유저 레벨 업데이트
        User user =
                userRepository.findById(userId).orElseThrow(() -> new UserException(USER_NOT_FOUND));
        Level level = calculateLevel(score);
        user.updateLevel(level);

        return LevelTestResultResponse.from(correctCount, level, wrongAnswers);
    }

    private Level calculateLevel(int score) {
        if (score <= 6) {
            return Level.BEGINNER;
        } else if (score <= 12) {
            return Level.INTERMEDIATE;
        } else {
            return Level.ADVANCED;
        }
    }
}
