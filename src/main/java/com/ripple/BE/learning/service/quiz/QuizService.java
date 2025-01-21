package com.ripple.BE.learning.service.quiz;

import static com.ripple.BE.learning.exception.errorcode.LearningErrorCode.*;

import com.ripple.BE.learning.domain.learningset.LearningSet;
import com.ripple.BE.learning.domain.learningset.UserLearningSet;
import com.ripple.BE.learning.domain.quiz.FailQuiz;
import com.ripple.BE.learning.domain.quiz.Quiz;
import com.ripple.BE.learning.domain.quiz.QuizScrap;
import com.ripple.BE.learning.domain.type.Type;
import com.ripple.BE.learning.dto.QuizDTO;
import com.ripple.BE.learning.dto.QuizListDTO;
import com.ripple.BE.learning.dto.QuizResultDTO;
import com.ripple.BE.learning.exception.LearningException;
import com.ripple.BE.learning.exception.QuizException;
import com.ripple.BE.learning.exception.errorcode.LearningErrorCode;
import com.ripple.BE.learning.repository.QuizRepository;
import com.ripple.BE.learning.repository.QuizScrapRepository;
import com.ripple.BE.learning.repository.UserLearningSetRepository;
import com.ripple.BE.learning.service.learningset.LearningSetService;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.domain.type.Level;
import com.ripple.BE.user.service.UserService;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class QuizService {

    private final UserLearningSetRepository userLearningSetRepository;
    private final QuizRepository quizRepository;
    private final QuizScrapRepository quizScrapRepository;

    private final QuizRedisService quizRedisService;
    private final LearningSetService learningSetService;
    private final UserService userService;

    private static final String QUESTION_TYPE = "questions";
    private static final String WRONG_ANSWER_TYPE = "wrongAnswer";
    private static final String QUIZ_COUNT = "quizCount";

    /**
     * 퀴즈 시작
     *
     * @param userId
     * @param learningSetId
     * @param level
     * @return 퀴즈 목록 반환
     */
    @Transactional
    public QuizListDTO startQuiz(final long userId, final long learningSetId, final Level level) {

        LearningSet learningSet = learningSetService.findLearningSetById(learningSetId);
        List<Quiz> quizList = quizRepository.findAllByLearningSetAndLevel(learningSet, level);

        quizList.forEach(
                quiz -> {
                    if (quiz.getType() != Type.OX) {
                        Collections.shuffle(quiz.getChoices());
                    }
                });

        QuizListDTO quizListDTO = QuizListDTO.toQuizListDTO(quizList);

        quizRedisService.saveToRedis(userId, QUESTION_TYPE, quizListDTO);
        quizRedisService.saveToRedis(userId, QUIZ_COUNT, quizList.size());

        return quizListDTO;
    }

    /**
     * 퀴즈 진행
     *
     * @param userId
     * @param quizId
     * @param answerIndex
     * @return 퀴즈 결과 반환(정답 여부, 정답 개수, 해설)
     */
    @Transactional
    public QuizResultDTO submitAnswer(final long userId, final long quizId, final int answerIndex) {

        QuizListDTO quizListDTO =
                quizRedisService.fetchFromRedis(userId, QUESTION_TYPE, QuizListDTO.class);

        // 마이페이지의 저장한 퀴즈에서 다시풀기 진행 시 (퀴즈 진행 redis 데이터가 없을 경우)
        if (quizListDTO == null) {
            Quiz quiz = getQuizById(quizId);
            boolean isCorrect = isCorrectAnswer(QuizDTO.toQuizDTO(quiz), answerIndex);

            return QuizResultDTO.toQuizResultDTO(isCorrect, quiz.getExplanation());
        }

        QuizDTO quizDTO = getQuizDTO(quizListDTO, quizId); // 퀴즈 정보 가져오기
        boolean isCorrect = isCorrectAnswer(quizDTO, answerIndex);

        if (!isCorrect) {
            quizRedisService.saveToRedisList(userId, WRONG_ANSWER_TYPE, quizId); // 오답 리스트에 추가
        }

        return QuizResultDTO.toQuizResultDTO(isCorrect, quizDTO.explanation());
    }

    /**
     * 퀴즈 종료 및 퀴즈 완료 처리
     *
     * @param userId
     * @param learningSetId
     * @param level
     * @return 퀴즈 결과 반환
     */
    @Transactional
    public void finishQuiz(final long userId, final long learningSetId, final Level level) {

        User user = userService.findUserById(userId);
        List<Integer> failList =
                quizRedisService.fetchListFromRedis(userId, WRONG_ANSWER_TYPE, Integer.class);
        UserLearningSet userLearningSet =
                userLearningSetRepository
                        .findByUserIdAndLearningSetIdAndLevel(userId, learningSetId, level)
                        .orElseThrow(() -> new LearningException(LearningErrorCode.LEARNING_SET_NOT_FOUND));
        List<FailQuiz> failQuizList =
                failList.stream().map(quizId -> FailQuiz.toFailQuiz(user, getQuizById(quizId))).toList();
        int quizCount = quizRedisService.fetchFromRedis(userId, QUIZ_COUNT, Integer.class);

        if (!userLearningSet.isQuizCompleted()) {
            int correctCount = quizCount - failList.size(); // 정답 개수 계산
            userLearningSet.setQuizCompleted(); // 퀴즈 완료 처리
            userService.updateUserStatsAfterQuiz(
                    user, level, failQuizList, quizCount, correctCount); // 사용자 통계 업데이트
        }

        quizRedisService.clearRedisKeys(userId); // 퀴즈 진행 관련 데이터 삭제
    }

    @Transactional(readOnly = true)
    public Quiz getQuizById(final long quizId) {
        return quizRepository
                .findById(quizId)
                .orElseThrow(() -> new LearningException(LearningErrorCode.QUIZ_NOT_FOUND));
    }

    // 퀴즈 정보 가져오기
    private QuizDTO getQuizDTO(final QuizListDTO quizListDTO, final long quizId) {
        return quizListDTO.quizList().stream()
                .filter(q -> q.id().equals(quizId))
                .findFirst()
                .orElseThrow(() -> new LearningException(QUIZ_PROGRESS_NOT_FOUND));
    }

    /**
     * 퀴즈 스크랩
     *
     * @param userId
     * @param quizId
     */
    @Transactional
    public void scrapQuiz(final long userId, final long quizId) {

        User user = userService.findUserById(userId);
        Quiz quiz =
                quizRepository.findById(quizId).orElseThrow(() -> new QuizException(QUIZ_NOT_FOUND));

        quizScrapRepository.save(QuizScrap.builder().user(user).quiz(quiz).build());
    }

    /**
     * 개별 퀴즈 조회
     *
     * @param quizId
     * @return 퀴즈 목록 반환
     */
    public QuizDTO getSingleQuiz(final long quizId) {
        Quiz quiz = getQuizById(quizId);
        return QuizDTO.toQuizDTO(quiz);
    }

    // 정답 여부 확인
    private boolean isCorrectAnswer(final QuizDTO quizDTO, final int answerIndex) {
        return quizDTO
                .answer()
                .trim()
                .equals(quizDTO.choiceList().choices().get(answerIndex).content().trim());
    }
}
