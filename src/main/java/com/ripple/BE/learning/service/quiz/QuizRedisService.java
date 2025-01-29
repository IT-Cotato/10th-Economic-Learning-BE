package com.ripple.BE.learning.service.quiz;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
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
    private static final String QUESTION_TYPE = "questions";
    private static final String QUIZ_COUNT = "quizCount";
    private static final String WRONG_ANSWER_TYPE = "wrongAnswer";
    private static final int QUIZ_TIME = 30; // 퀴즈 진행 시간

    // Redis에 데이터 저장
    protected <T> void saveToRedis(final long userId, final String type, final T data) {
        redisTemplate.opsForValue().set(getRedisKey(userId, type), data, Duration.ofMinutes(QUIZ_TIME));
    }

    // Redis 리스트에 저장 및 조회 추가
    protected <T> void saveToRedisList(final long userId, final String type, final T data) {
        redisTemplate.opsForList().rightPush(getRedisKey(userId, type), data); // RPUSH로 추가
        redisTemplate.expire(getRedisKey(userId, type), Duration.ofMinutes(QUIZ_TIME)); // 만료 시간 설정
    }

    // Redis에서 데이터 가져오기
    protected <T> T fetchFromRedis(final long userId, final String type, final Class<T> clazz) {
        return clazz.cast(redisTemplate.opsForValue().get(getRedisKey(userId, type)));
    }

    // Redis에서 리스트 가져오기
    protected <T> List<T> fetchListFromRedis(
            final long userId, final String type, final Class<T> clazz) {
        List<Object> rawList =
                redisTemplate.opsForList().range(getRedisKey(userId, type), 0, -1); // 전체 조회
        if (rawList == null || rawList.isEmpty()) {
            return Collections.emptyList(); // 빈 리스트 반환
        }

        return rawList.stream().map(clazz::cast).collect(Collectors.toList());
    }

    // Redis에 저장된 퀴즈 진행 관련 데이터 삭제
    protected void clearRedisKeys(final long userId) {
        redisTemplate.delete(getRedisKey(userId, QUESTION_TYPE));
        redisTemplate.delete(getRedisKey(userId, QUIZ_COUNT));
        redisTemplate.delete(getRedisKey(userId, WRONG_ANSWER_TYPE));
    }

    // Redis 키 생성
    protected String getRedisKey(final long userId, final String type) {
        return String.format(QUIZ_KEY_TEMPLATE, userId, type);
    }
}
