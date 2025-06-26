package com.ripple.BE.post.application.cache.redis;

import static com.ripple.BE.post.application.cache.PostCacheKey.*;

import com.ripple.BE.post.application.cache.PostCacheManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostCacheEvictionSubscriber {

    private final PostCacheManager postCacheManager;

    /** Redis에서 캐시 무효화 메시지를 수신하면 실행 */
    public void onEvictMessage(String message) {
        log.info("[RedisCacheEvict] 수신된 키: {}", message);
        if (POST_EVICT_ALL.equals(message)) {
            postCacheManager.evictAll();
        } else if (TOKTOK_EVICT_ALL.equals(message)) {
            postCacheManager.evictAllToktok();
        } else {
            postCacheManager.evict(message);
        }
    }
}
