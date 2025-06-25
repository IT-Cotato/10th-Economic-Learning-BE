package com.ripple.BE.post.application.cache.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.ripple.BE.post.application.cache.PostCache;
import com.ripple.BE.post.dto.response.PostPreviewListResponseDTO;
import com.ripple.BE.post.dto.response.PostPreviewResponseDTO;
import com.ripple.BE.post.dto.response.ToktokPreviewListResponseDTO;
import java.time.Duration;
import java.util.List;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
@Component
public class CaffeineCache implements PostCache {

    private final Cache<String, PostPreviewListResponseDTO> postCache =
            Caffeine.newBuilder().maximumSize(1000).expireAfterWrite(Duration.ofMinutes(1)).build();

    private final Cache<String, List<PostPreviewResponseDTO>> popularCache =
            Caffeine.newBuilder().maximumSize(100).expireAfterWrite(Duration.ofMinutes(1)).build();

    private final Cache<String, ToktokPreviewListResponseDTO> toktokCache =
            Caffeine.newBuilder().maximumSize(1000).expireAfterWrite(Duration.ofMinutes(1)).build();

    @Override
    public PostPreviewListResponseDTO getPostList(String key) {
        return postCache.getIfPresent(key);
    }

    @Override
    public void putPostList(String key, PostPreviewListResponseDTO value) {
        postCache.put(key, value);
    }

    @Override
    public ToktokPreviewListResponseDTO getToktokList(String key) {
        return toktokCache.getIfPresent(key);
    }

    @Override
    public void putToktokList(String key, ToktokPreviewListResponseDTO value) {
        toktokCache.put(key, value);
    }

    @Override
    public void evict(String key) {
        postCache.invalidate(key);
        popularCache.invalidate(key);
        toktokCache.invalidate(key);
    }

    @Override
    public void clear() {
        postCache.invalidateAll();
        popularCache.invalidateAll();
    }
}
