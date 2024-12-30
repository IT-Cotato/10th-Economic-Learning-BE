package com.ripple.BE.learning.service;

import static com.ripple.BE.learning.exception.errorcode.LearningErrorCode.*;

import com.ripple.BE.learning.domain.LearningSet;
import com.ripple.BE.learning.domain.LearningSetComplete;
import com.ripple.BE.learning.domain.Quiz;
import com.ripple.BE.learning.domain.type.Type;
import com.ripple.BE.learning.dto.QuizDTO;
import com.ripple.BE.learning.dto.QuizListDTO;
import com.ripple.BE.learning.dto.QuizResultDTO;
import com.ripple.BE.learning.exception.LearningException;
import com.ripple.BE.learning.exception.errorcode.LearningErrorCode;
import com.ripple.BE.learning.repository.LearningSetCompleteRepository;
import com.ripple.BE.learning.repository.QuizRepository;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.domain.type.Level;
import com.ripple.BE.user.service.UserService;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class QuizService {

    private final LearningSetCompleteRepository learningSetCompleteRepository;
    private final QuizRepository quizRepository;

    private final LearningSetService learningSetService;
    private final UserService userService;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String QUIZ_KEY_TEMPLATE = "quiz:%d:%s"; // Redis 키 템플릿, quiz:{userId}:{type}
    private static final String LEARNING_SET_ID_TYPE = "learningSetId";
    private static final String QUESTION_TYPE = "questions";
    private static final String STATE_TYPE = "state";

    private static final int QUIZ_TIME = 30; // 퀴즈 진행 시간
    private static final int QUIZ_COUNT = 3; // 퀴즈 개수
    private static final int INITIAL_STATE = 0; // 퀴즈 진행 상태 초기값
    private static final int OTHER_OPTIONS_COUNT = 2; // 다른 퀴즈 보기 선지 개수

    /**
     * 퀴즈 시작
     *
     * @param userId
     * @param learningSetName
     * @param level
     * @return 퀴즈 목록 반환
     */
    @Transactional
    public QuizListDTO startQuiz(final long userId, final String learningSetName, final Level level) {
        LearningSet learningSet =
                learningSetService.findLearningSetByNameAndLevel(learningSetName, level);

        List<QuizDTO> quizDTOList = createQuizList(learningSet);

        QuizListDTO quizListDTO = QuizListDTO.fromQuidDTOList(quizDTOList);

        saveToRedis(userId, QUESTION_TYPE, quizListDTO); // 퀴즈 목록 저장
        saveToRedis(userId, STATE_TYPE, INITIAL_STATE); // 퀴즈 진행 상태 초기값 저장
        saveToRedis(userId, LEARNING_SET_ID_TYPE, String.valueOf(learningSet.getId())); // 학습 세트 ID 저장

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

        QuizListDTO quizListDTO = fetchFromRedis(userId, QUESTION_TYPE, QuizListDTO.class);
        Integer correctCount = fetchFromRedis(userId, STATE_TYPE, Integer.class);
        Long learningSetId = Long.valueOf(fetchFromRedis(userId, LEARNING_SET_ID_TYPE, String.class));

        validateQuizState(quizListDTO, correctCount, learningSetId);
        QuizDTO quizDTO = getQuizDTO(quizListDTO, quizId); // 퀴즈 정보 가져오기

        boolean isCorrect = quizDTO.answer().equals(quizDTO.options().get(answerIndex)); // 정답 여부 판별
        correctCount = isCorrect ? correctCount + 1 : correctCount;
        saveToRedis(userId, STATE_TYPE, correctCount); // 정답 개수 저장

        return QuizResultDTO.toQuizResultDTO(isCorrect, correctCount, quizDTO.explanation());
    }

    /**
     * 퀴즈 종료 및 퀴즈 완료 처리
     *
     * @param userId
     * @return
     */
    @Transactional
    public QuizResultDTO finishQuiz(final long userId) {
        User user = userService.findUserById(userId);
        Integer correctCount = fetchFromRedis(userId, STATE_TYPE, Integer.class);
        Long learningSetId = Long.valueOf(fetchFromRedis(userId, LEARNING_SET_ID_TYPE, String.class));

        validateQuizState(null, correctCount, learningSetId); // 퀴즈 상태 유효성 검사

        LearningSetComplete learningSetComplete =
                learningSetCompleteRepository
                        .findByUserIdAndLearningSetId(userId, learningSetId)
                        .orElseThrow(() -> new LearningException(LearningErrorCode.LEARNING_SET_NOT_FOUND));

        learningSetComplete.setQuizCompleted(true); // 퀴즈 완료 처리
        userService.updateQuizAndCorrectCount(user, QUIZ_COUNT, correctCount); // 퀴즈 횟수 및 정답 개수 업데이트

        clearRedisKeys(userId); // 퀴즈 진행 관련 데이터 삭제
        return QuizResultDTO.toQuizResultDTO(null, correctCount, null);
    }

    @Transactional(readOnly = true)
    public Quiz getQuizById(final long quizId) {
        return quizRepository
                .findById(quizId)
                .orElseThrow(() -> new LearningException(LearningErrorCode.QUIZ_NOT_FOUND));
    }

    // 퀴즈 목록 생성
    private List<QuizDTO> createQuizList(LearningSet learningSet) {
        List<Quiz> quizList = learningSet.getQuizzes();
        Collections.shuffle(quizList);

        // 퀴즈 목록에서 QUIZ_COUNT만큼 랜덤으로 퀴즈를 가져와서 무작위 보기 생성
        return quizList.stream()
                .limit(QUIZ_COUNT)
                .map(quiz -> createRandomOptions(quiz, quizList))
                .toList();
    }

    private QuizDTO createRandomOptions(final Quiz quiz, final List<Quiz> quizList) {
        List<String> options;

        if (quiz.getType() == Type.OX) {
            options = List.of("O", "X"); // OX 퀴즈는 보기가 고정
        } else {
            options = generateOptions(quiz, quizList);
            options =
                    Stream.concat(options.stream(), Stream.of(quiz.getAnswer(), quiz.getWrongAnswer()))
                            .filter(Objects::nonNull)
                            .distinct()
                            .collect(Collectors.toList());
            Collections.shuffle(options); // 보기 섞기
        }

        return QuizDTO.toQuizDTO(quiz, options);
    }

    // 다른 퀴즈 보기 선지 생성
    private List<String> generateOptions(Quiz quiz, List<Quiz> quizList) {
        return quizList.stream()
                .filter(q -> !q.getId().equals(quiz.getId()))
                .filter(q -> q.getType() == quiz.getType())
                .flatMap(q -> Stream.of(q.getAnswer(), q.getWrongAnswer()))
                .filter(Objects::nonNull)
                .distinct()
                .limit(OTHER_OPTIONS_COUNT)
                .collect(Collectors.toList());
    }

    // 퀴즈 정보 가져오기
    private QuizDTO getQuizDTO(final QuizListDTO quizListDTO, final long quizId) {
        return quizListDTO.quizList().stream()
                .filter(q -> q.id().equals(quizId))
                .findFirst()
                .orElseThrow(() -> new LearningException(QUIZ_PROGRESS_NOT_FOUND));
    }

    // 퀴즈 상태 유효성 검사, 퀴즈 진행 상태, 정답 개수, 학습 세트 ID가 null이면 예외 발생
    private void validateQuizState(
            QuizListDTO quizListDTO, Integer correctCount, Long learningSetId) {
        if ((quizListDTO != null && quizListDTO.quizList().isEmpty())
                || correctCount == null
                || learningSetId == null) {
            throw new LearningException(QUIZ_PROGRESS_NOT_FOUND);
        }
    }

    // Redis에 데이터 저장
    private <T> void saveToRedis(final long userId, final String type, final T data) {
        redisTemplate.opsForValue().set(getRedisKey(userId, type), data, Duration.ofMinutes(QUIZ_TIME));
    }

    // Redis에서 데이터 가져오기
    private <T> T fetchFromRedis(final long userId, final String type, final Class<T> clazz) {
        return clazz.cast(redisTemplate.opsForValue().get(getRedisKey(userId, type)));
    }

    // Redis에 저장된 퀴즈 진행 관련 데이터 삭제
    private void clearRedisKeys(final long userId) {
        redisTemplate.delete(getRedisKey(userId, QUESTION_TYPE));
        redisTemplate.delete(getRedisKey(userId, STATE_TYPE));
        redisTemplate.delete(getRedisKey(userId, LEARNING_SET_ID_TYPE));
    }

    // Redis 키 생성
    private String getRedisKey(final long userId, final String type) {
        return String.format(QUIZ_KEY_TEMPLATE, userId, type);
    }
}
