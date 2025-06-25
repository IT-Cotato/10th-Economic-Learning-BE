package com.ripple.BE.learning.application.quiz;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class QuizRedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String QUIZ_KEY_TEMPLATE = "quiz:%d:%s"; // Redis 키 템플릿, quiz:{userId}:{type}
    private static final String LEVEL_TEST_KEY_TEMPLATE =
            "levelTest:%s:%s"; // 레벨 테스트 키 템플릿, levelTest:{key}:{type}
    private static final String QUESTION_TYPE = "questions";
    private static final String QUIZ_COUNT = "quizCount";
    private static final String LEVEL_TEST_ANSWER_TYPE = "levelTestAnswers"; // 레벨 테스트 답안 저장
    private static final String WRONG_ANSWER_TYPE = "wrongAnswer";
    private static final int QUIZ_TIME = 30; // 퀴즈 진행 시간

    // Redis에 데이터 저장
    protected <T> void saveToRedis(final long userId, final String type, final T data) {
        redisTemplate.opsForValue().set(getRedisKey(userId, type), data, Duration.ofMinutes(QUIZ_TIME));
    }

    protected <T> void saveToRedisSet(final long userId, final String type, final T data) {
        redisTemplate.opsForSet().add(getRedisKey(userId, type), data);
        redisTemplate.expire(getRedisKey(userId, type), Duration.ofMinutes(QUIZ_TIME)); // 만료 시간 설정
    }

    // Redis에 Map 저장
    protected <K, V> void saveMapToRedis(final String key, final String type, final Map<K, V> data) {
        String newKey = getRedisKey(key, type);
        redisTemplate.opsForHash().putAll(newKey, data);
        redisTemplate.expire(key, Duration.ofMinutes(QUIZ_TIME));
    }

    // Redis에서 데이터 가져오기
    protected <T> T fetchFromRedis(final long userId, final String type, final Class<T> clazz) {
        return clazz.cast(redisTemplate.opsForValue().get(getRedisKey(userId, type)));
    }

    // Redis에서 Set 조회
    protected <T> Set<T> fetchFromRedisSet(
            final long userId, final String type, final Class<T> clazz) {
        Set<Object> rawSet = redisTemplate.opsForSet().members(getRedisKey(userId, type));
        if (rawSet == null || rawSet.isEmpty()) {
            return Collections.emptySet();
        }
        return rawSet.stream().map(clazz::cast).collect(Collectors.toSet());
    }

    // Redis에서 Map 조회
    @SuppressWarnings("unchecked")
    protected <K, V> Map<K, V> fetchMapFromRedis(final String levelTestKey, final String type) {
        String key = getRedisKey(levelTestKey, type);
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);

        if (entries.isEmpty()) {
            return Collections.emptyMap();
        }
        return entries.entrySet().stream()
                .collect(Collectors.toMap(e -> (K) e.getKey(), e -> (V) e.getValue()));
    }

    // Redis에 저장된 퀴즈 진행 관련 데이터 삭제
    protected void clearRedisKeys(final long userId) {
        redisTemplate.delete(getRedisKey(userId, QUESTION_TYPE));
        redisTemplate.delete(getRedisKey(userId, QUIZ_COUNT));
        redisTemplate.delete(getRedisKey(userId, WRONG_ANSWER_TYPE));
    }

    protected void clearRedisKeys(final String key) {
        redisTemplate.delete(getRedisKey(key, LEVEL_TEST_ANSWER_TYPE));
    }

    // Redis 키 생성
    protected String getRedisKey(final long userId, final String type) {
        return String.format(QUIZ_KEY_TEMPLATE, userId, type);
    }

    protected String getRedisKey(final String key, final String type) {
        return String.format(LEVEL_TEST_KEY_TEMPLATE, key, type);
    }
}
