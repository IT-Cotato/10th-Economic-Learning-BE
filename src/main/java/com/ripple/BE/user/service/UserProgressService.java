package com.ripple.BE.user.service;

import com.ripple.BE.learning.persistence.jpa.repository.concept.ConceptJpaRepository;
import com.ripple.BE.learning.persistence.jpa.repository.learningset.UserLearningSetJpaRepository;
import com.ripple.BE.learning.persistence.jpa.repository.quiz.QuizJpaRepository;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.domain.type.Level;
import com.ripple.BE.user.dto.ProgressDTO;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class UserProgressService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String PROGRESS_KEY_PREFIX = "progress:";
    private static final String TOTAL_SETS_KEY = "totalSets";

    private static final int PROGRESS_CACHE_EXPIRE_MINUTES = 10;
    private static final int TOTAL_SETS_CACHE_EXPIRE_DAYS = 1;

    private final UserService userService;

    private final UserLearningSetJpaRepository userLearningSetJpaRepository;
    private final ConceptJpaRepository conceptJpaRepository;
    private final QuizJpaRepository quizJpaRepository;

    /**
     * 레벨별 총 학습 세트 수를 조회한다. 개념 + 퀴즈 이 데이터는 캐시에 저장되며, 1일간 유효하다.
     *
     * @return 레벨별 총 학습 세트 수
     */
    public Map<String, Integer> getTotalLearningSetsCount() {
        Map<String, Integer> totalSets =
                (Map<String, Integer>) redisTemplate.opsForValue().get(TOTAL_SETS_KEY);

        if (totalSets == null) {
            totalSets =
                    Arrays.stream(Level.values())
                            .collect(
                                    Collectors.toMap(
                                            Enum::name,
                                            level ->
                                                    conceptJpaRepository.countByLevel(level)
                                                            + quizJpaRepository.countByLevel(level)));

            redisTemplate
                    .opsForValue()
                    .set(TOTAL_SETS_KEY, totalSets, TOTAL_SETS_CACHE_EXPIRE_DAYS, TimeUnit.DAYS);
        }

        return totalSets;
    }

    /**
     * 유저의 레벨별 학습 완료율을 조회한다. 이 데이터는 캐시에 저장되며, 10분간 유효하다.
     *
     * @param userId 유저 ID
     * @return 레벨별 학습 완료율
     */
    public ProgressDTO getLearningSetCompletionRate(final long userId) {
        User user = userService.findUserById(userId);
        String cacheKey = PROGRESS_KEY_PREFIX + userId;

        Map<String, Integer> totalSets = getTotalLearningSetsCount();
        ProgressDTO cachedProgress = (ProgressDTO) redisTemplate.opsForValue().get(cacheKey);

        // 캐시된 데이터가 있으면 바로 반환
        if (cachedProgress != null) {
            return cachedProgress;
        }

        // 레벨별 완료율 계산, 퍼센트 단위로 변환
        ProgressDTO progressDTO =
                ProgressDTO.toProgressDTO(
                        Arrays.stream(Level.values())
                                .collect(
                                        Collectors.toMap(
                                                level -> level,
                                                level ->
                                                        (double) user.getCompletedCountByLevel(level)
                                                                / totalSets.get(level.name())
                                                                * 100)));

        // 캐싱 (10분간 저장)
        redisTemplate
                .opsForValue()
                .set(cacheKey, progressDTO, PROGRESS_CACHE_EXPIRE_MINUTES, TimeUnit.MINUTES);

        return progressDTO;
    }
}
