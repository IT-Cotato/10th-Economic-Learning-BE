package com.ripple.BE.post.application.cache.impl;

import com.ripple.BE.post.application.cache.PostCache;
import com.ripple.BE.post.application.cache.PostCacheKey;
import com.ripple.BE.post.dto.response.PostPreviewListResponseDTO;
import com.ripple.BE.post.dto.response.ToktokPreviewListResponseDTO;
import java.time.Duration;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Order(2)
@Component
@RequiredArgsConstructor
public class RedisCache implements PostCache {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final Duration TTL = Duration.ofMinutes(3);

    @Override
    public PostPreviewListResponseDTO getPostList(String key) {
        return (PostPreviewListResponseDTO) redisTemplate.opsForValue().get(key);
    }

    @Override
    public ToktokPreviewListResponseDTO getToktokList(String key) {
        return (ToktokPreviewListResponseDTO) redisTemplate.opsForValue().get(key);
    }

    @Override
    public void putPostList(String key, PostPreviewListResponseDTO value) {
        redisTemplate.opsForValue().set(key, value, TTL);
    }

    @Override
    public void putToktokList(String key, ToktokPreviewListResponseDTO value) {
        redisTemplate.opsForValue().set(key, value, TTL);
    }

    @Override
    public void evict(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void clear() {
        redisTemplate.delete(
                Objects.requireNonNull(redisTemplate.keys(PostCacheKey.POST_LIST_PREFIX + "*")));
    }
}
