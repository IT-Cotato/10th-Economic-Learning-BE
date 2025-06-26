package com.ripple.BE.post.application.cache;

import com.ripple.BE.post.dto.response.PostPreviewListResponseDTO;
import com.ripple.BE.post.dto.response.PostPreviewResponseDTO;
import com.ripple.BE.post.dto.response.ToktokPreviewListResponseDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostCacheManager {

    private final List<PostCache> caches;

    public PostPreviewListResponseDTO getPosts(String key) {
        for (PostCache cache : caches) {
            PostPreviewListResponseDTO cached = cache.getPostList(key);
            if (cached != null) return cached;
        }
        return null;
    }

    public void putPosts(String key, PostPreviewListResponseDTO value) {
        for (PostCache cache : caches) {
            cache.putPostList(key, value);
        }
    }

    public ToktokPreviewListResponseDTO getToktoks(String key) {
        for (PostCache cache : caches) {
            ToktokPreviewListResponseDTO cached = cache.getToktokList(key);
            if (cached != null) return cached;
        }
        return null;
    }

    public void putToktoks(String key, ToktokPreviewListResponseDTO value) {
        for (PostCache cache : caches) {
            cache.putToktokList(key, value);
        }
    }

    public List<PostPreviewResponseDTO> getPopular(String key) {
        for (PostCache cache : caches) {
            PostPreviewListResponseDTO cached = cache.getPostList(key);
            if (cached != null) return cached.postPreviewList();
        }
        return null;
    }

    public void putPopular(String key, List<PostPreviewResponseDTO> value) {
        PostPreviewListResponseDTO dto = PostPreviewListResponseDTO.of(value, 1, 0);
        for (PostCache cache : caches) {
            cache.putPostList(key, dto);
        }
    }

    public void evict(String key) {
        caches.forEach(cache -> cache.evict(key));
    }

    public void evictAll() {
        caches.forEach(PostCache::clear);
    }

    public void evictAllToktok() {
        caches.forEach(cache -> cache.evict(PostCacheKey.TOKTOK_LIST_PREFIX + "*"));
    }
}
