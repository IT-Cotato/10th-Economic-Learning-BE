package com.ripple.BE.learning.application.quiz;

import com.ripple.BE.learning.dto.response.leveltest.LevelTestQuizAnswerDTO;
import com.ripple.BE.learning.dto.response.quiz.RandomQuizResponseListDTO;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizSessionCacheManager {

    private final QuizRedisService redis;
    private static final String QUESTION_TYPE = "questions";
    private static final String LEVEL_TEST_ANSWER_TYPE = "levelTestAnswers";
    private static final String WRONG_ANSWER_TYPE = "wrongAnswer";
    private static final String QUIZ_COUNT = "quizCount";

    /** 퀴즈 세션 저장 */
    public void storeQuizSession(long userId, RandomQuizResponseListDTO questions, long quizCount) {
        redis.saveToRedis(userId, QUESTION_TYPE, questions);
        redis.saveToRedis(userId, QUIZ_COUNT, quizCount);
    }

    /** 레벨 테스트 퀴즈 세션 저장 */
    public void storeLevelTestQuizSession(String key, Map<Long, LevelTestQuizAnswerDTO> answers) {
        redis.saveMapToRedis(key, LEVEL_TEST_ANSWER_TYPE, answers);
    }

    /** 틀린 퀴즈 저장 */
    public void storeWrongAnswer(Long userId, Long quizId) {
        redis.saveToRedisSet(userId, WRONG_ANSWER_TYPE, String.valueOf(quizId));
    }

    /** 퀴즈 세션 조회 */
    public RandomQuizResponseListDTO getQuizQuestions(long userId) {
        return redis.fetchFromRedis(userId, QUESTION_TYPE, RandomQuizResponseListDTO.class);
    }

    /** 레벨 테스트 퀴즈 세션 조회 */
    public Map<Long, LevelTestQuizAnswerDTO> getLevelTestQuizQuestions(String levelTestKey) {
        return redis.fetchMapFromRedis(levelTestKey, LEVEL_TEST_ANSWER_TYPE);
    }

    /** 틀린 퀴즈 목록 조회 */
    public Set<Long> getWrongAnswerSet(long userId) {
        Set<String> wrongAnswers = redis.fetchFromRedisSet(userId, WRONG_ANSWER_TYPE, String.class);

        return wrongAnswers.stream().map(Long::parseLong).collect(Collectors.toSet());
    }

    public Integer getQuizCount(long userId) {
        return redis.fetchFromRedis(userId, QUIZ_COUNT, Integer.class);
    }

    public void clearAll(long userId) {
        redis.clearRedisKeys(userId);
    }

    public void clearLevelTestSession(String levelTestKey) {
        redis.clearRedisKeys(levelTestKey);
    }
}
