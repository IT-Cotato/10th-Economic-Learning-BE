package com.ripple.BE.learning.application.quiz;

import static com.ripple.BE.learning.exception.errorcode.LearningErrorCode.*;

import com.ripple.BE.learning.application.learningset.LearningSetService;
import com.ripple.BE.learning.domain.learningset.UserLearningSet;
import com.ripple.BE.learning.domain.quiz.FailQuiz;
import com.ripple.BE.learning.domain.quiz.Quiz;
import com.ripple.BE.learning.domain.quiz.QuizScrap;
import com.ripple.BE.learning.dto.response.quiz.QuizCompletionDTO;
import com.ripple.BE.learning.dto.response.quiz.QuizResponseDTO;
import com.ripple.BE.learning.dto.response.quiz.QuizResultResponseDTO;
import com.ripple.BE.learning.dto.response.quiz.RandomQuizResponseDTO;
import com.ripple.BE.learning.dto.response.quiz.RandomQuizResponseListDTO;
import com.ripple.BE.learning.exception.LearningException;
import com.ripple.BE.learning.exception.errorcode.LearningErrorCode;
import com.ripple.BE.learning.persistence.QuizRepository;
import com.ripple.BE.learning.persistence.QuizScrapRepository;
import com.ripple.BE.learning.persistence.UserLearningSetRepository;
import com.ripple.BE.user.domain.type.Level;
import com.ripple.BE.user.service.UserProgressManager;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizScrapRepository quizScrapRepository;
    private final UserLearningSetRepository userLearningSetRepository;

    private final UserProgressManager userProgressManager;
    private final QuizSessionCacheManager quizSessionCacheManager;

    private final LearningSetService learningSetService;

    /**
     * 퀴즈 시작
     *
     * @return 퀴즈 목록 반환
     */
    public List<RandomQuizResponseDTO> startQuiz(
            final long userId, final long learningSetId, final Level level) {

        // 퀴즈 정보 조회
        List<Quiz> quizList = quizRepository.findAllByLearningSetIdAndLevel(learningSetId, level);

        if (quizList.isEmpty()) {
            throw new LearningException(LearningErrorCode.QUIZ_NOT_FOUND);
        }

        // 퀴즈 선지 무작위 섞기
        quizList = quizList.stream().map(Quiz::shuffleChoices).toList();

        // 퀴즈 목록 반환 DTO 변환
        List<RandomQuizResponseDTO> randomQuizResponseDTOList =
                quizList.stream().map(RandomQuizResponseDTO::from).toList();

        // Redis에 퀴즈 진행 정보 저장
        quizSessionCacheManager.storeQuizSession(
                userId, RandomQuizResponseListDTO.from(randomQuizResponseDTOList), quizList.size());

        return randomQuizResponseDTOList;
    }

    /**
     * 퀴즈 진행 중 답안 제출
     *
     * @return 퀴즈 결과 반환(정답 여부, 해설)
     */
    public QuizResultResponseDTO submitAnswer(
            final long userId, final long quizId, final int answerIndex) {

        // 퀴즈 ID로 퀴즈 정보 조회
        Quiz quiz =
                quizRepository.findById(quizId).orElseThrow(() -> new LearningException(QUIZ_NOT_FOUND));

        // 캐시에서 사용자 퀴즈 목록 조회
        List<RandomQuizResponseDTO> quizList =
                quizSessionCacheManager.getQuizQuestions(userId).quizzes();

        // 퀴즈 ID로 퀴즈 정보 찾기
        RandomQuizResponseDTO quizResponseDTO =
                quizList.stream()
                        .filter(q -> q.quizId().equals(quizId))
                        .findFirst()
                        .orElseThrow(() -> new LearningException(QUIZ_PROGRESS_NOT_FOUND));

        // 퀴즈 답과 사용자 선택지 비교하여 정답 여부 확인
        boolean isCorrect = quiz.isCorrectAnswer(quizResponseDTO.choices().get(answerIndex).content());

        // 틀린 퀴즈는 Redis에 저장
        if (!isCorrect) {
            quizSessionCacheManager.storeWrongAnswer(userId, quizId);
        }

        return QuizResultResponseDTO.of(isCorrect, quiz);
    }

    /**
     * 저장한 퀴즈 다시 풀기
     *
     * @return 퀴즈 결과 반환(정답 여부, 해설)
     */
    public QuizResultResponseDTO retryScrapQuiz(final long quizId, final int answerIndex) {
        Quiz quiz =
                quizRepository.findById(quizId).orElseThrow(() -> new LearningException(QUIZ_NOT_FOUND));

        // 정답 여부 확인
        boolean isCorrect = quiz.isCorrectAnswer(quiz.getChoices().get(answerIndex).getContent());

        return QuizResultResponseDTO.of(isCorrect, quiz);
    }

    /** 퀴즈 종료 */
    @Transactional
    public void finishQuiz(final long userId) {
        // 사용자 퀴즈 세션에서 틀린 문제 목록과 퀴즈 개수 조회
        Set<Long> failSet = quizSessionCacheManager.getWrongAnswerSet(userId);
        Integer quizCount = quizSessionCacheManager.getQuizCount(userId);
        RandomQuizResponseListDTO quizListDTO = quizSessionCacheManager.getQuizQuestions(userId);

        if (quizListDTO == null || quizListDTO.quizzes() == null || quizListDTO.quizzes().isEmpty()) {
            throw new LearningException(QUIZ_PROGRESS_NOT_FOUND);
        }

        // 첫 번째 퀴즈 응답 DTO를 가져옴
        RandomQuizResponseDTO quizResponseDTO = quizListDTO.quizzes().get(0);

        // 퀴즈 세션 캐시 삭제
        quizSessionCacheManager.clearAll(userId);

        if (quizResponseDTO == null) {
            throw new LearningException(LearningErrorCode.QUIZ_PROGRESS_NOT_FOUND);
        }

        // 퀴즈 정보 조회
        Quiz quiz =
                quizRepository
                        .findById(quizResponseDTO.quizId())
                        .orElseThrow(() -> new LearningException(LearningErrorCode.QUIZ_NOT_FOUND));

        // 틀린 문제 목록
        List<FailQuiz> failQuizList =
                failSet.stream().map(id -> FailQuiz.withoutId(userId, id)).toList();

        // 사용자 상태 업데이트
        userProgressManager.updateAfterQuiz(
                userId, quiz.getLearningSetId(), quiz.getLevel(), failQuizList, quizCount);
    }

    /** 퀴즈 스크랩 */
    @Transactional
    public void scrapQuiz(final long userId, final long quizId) {
        // 이미 스크랩한 퀴즈인지 확인
        if (quizScrapRepository.existsByQuizIdAndUserId(quizId, userId)) {
            throw new LearningException(LearningErrorCode.QUIZ_ALREADY_SCRAP);
        }
        if (!quizRepository.existsById(quizId)) {
            throw new LearningException(LearningErrorCode.QUIZ_NOT_FOUND);
        }

        QuizScrap quizScrap = QuizScrap.withoutId(userId, quizId);
        quizScrapRepository.save(quizScrap);
    }

    /** 퀴즈 스크랩 삭제 */
    @Transactional
    public void removeScrapFromQuiz(final long quizId, final long userId) {
        QuizScrap quizScrap =
                quizScrapRepository
                        .findByQuizIdAndUserId(quizId, userId)
                        .orElseThrow(() -> new LearningException(LearningErrorCode.QUIZ_SCRAP_NOT_FOUND));

        quizScrapRepository.delete(quizScrap);
    }

    /** 개별 퀴즈 조회 */
    public QuizResponseDTO getSingleQuiz(final long quizId) {
        Quiz quiz =
                quizRepository
                        .findById(quizId)
                        .orElseThrow(() -> new LearningException(LearningErrorCode.QUIZ_NOT_FOUND));

        return QuizResponseDTO.from(quiz);
    }

    public QuizCompletionDTO getCompleteQuizzesByLearningSet(
            final long userId, final long learningSetId) {

        boolean beginnerCompleted =
                getUserLearningSet(userId, learningSetId, Level.BEGINNER).isQuizCompleted();
        boolean intermediateCompleted =
                getUserLearningSet(userId, learningSetId, Level.INTERMEDIATE).isQuizCompleted();
        boolean advancedCompleted =
                getUserLearningSet(userId, learningSetId, Level.ADVANCED).isQuizCompleted();

        return QuizCompletionDTO.of(beginnerCompleted, intermediateCompleted, advancedCompleted);
    }

    private UserLearningSet getUserLearningSet(long userId, long learningSetId, Level level) {
        return userLearningSetRepository
                .findByUserIdAndLearningSetIdAndLevel(userId, learningSetId, level)
                .orElseThrow(() -> new LearningException(LEARNING_SET_NOT_FOUND));
    }
}
