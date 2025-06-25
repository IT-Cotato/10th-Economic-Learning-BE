package com.ripple.BE.post.application.cache.redis;

import static com.ripple.BE.post.application.cache.PostCacheKey.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostCacheEvictionPublisher {

    private final RedisTemplate<String, String> redisTemplate;
    private final ChannelTopic postCacheEvictTopic;

    public void publishEvict(String key) {
        log.info("Publishing cache evict key to Redis topic: {}", postCacheEvictTopic.getTopic());
        redisTemplate.convertAndSend(postCacheEvictTopic.getTopic(), key);
    }

    public void publishEvictAll() {
        redisTemplate.convertAndSend(postCacheEvictTopic.getTopic(), POST_EVICT_ALL);
    }

    public void publishEvictAllToktok() {
        redisTemplate.convertAndSend(postCacheEvictTopic.getTopic(), TOKTOK_EVICT_ALL);
    }
}
