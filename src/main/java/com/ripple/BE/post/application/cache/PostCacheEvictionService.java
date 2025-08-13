package com.ripple.BE.post.application.cache;

import com.ripple.BE.post.application.cache.redis.PostCacheEvictionPublisher;
import com.ripple.BE.post.domain.post.Post;
import com.ripple.BE.post.domain.type.PostSort;
import com.ripple.BE.post.dto.response.PostPreviewListResponseDTO;
import com.ripple.BE.post.dto.response.PostPreviewResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostCacheEvictionService {

    private static final int MAX_CACHED_PAGES = 3;

    private final PostCacheManager postCacheManager;
    private final PostCacheEvictionPublisher cacheEvictionPublisher;

    public void evictPostCachesIfContained(Post post) {
        log.debug("Checking cache eviction for post: {} (type: {})", post.getId(), post.getType());

        // 상위 3페이지 캐시를 확인하고 해당 게시물이 포함된 페이지만 무효화
        for (int page = 0; page < MAX_CACHED_PAGES; page++) {
            evictCacheIfPostContained(post, PostSort.RECENT, page);
            evictCacheIfPostContained(post, PostSort.POPULAR, page);
        }

        // 인기 게시글 캐시도 확인하고 무효화
        evictPopularCacheIfPostContained(post);
    }

    private void evictCacheIfPostContained(Post post, PostSort sort, int page) {
        String key = PostCacheKey.generatePostListKey(post.getType(), sort, page);
        PostPreviewListResponseDTO cachedList = postCacheManager.getPosts(key);

        if (cachedList != null && isPostInCache(post.getId(), cachedList)) {
            log.info("Evicting cache for key: {} (contains post {})", key, post.getId());
            cacheEvictionPublisher.publishEvict(key);
        }
    }

    private void evictPopularCacheIfPostContained(Post post) {
        String key = PostCacheKey.POPULAR_POSTS;
        var cachedPopular = postCacheManager.getPopular(key);

        if (cachedPopular != null && isPostInPopularCache(post.getId(), cachedPopular)) {
            log.info("Evicting popular cache (contains post {})", post.getId());
            cacheEvictionPublisher.publishEvict(key);
        }
    }

    private boolean isPostInCache(Long postId, PostPreviewListResponseDTO cachedList) {
        return cachedList.postPreviewList().stream().anyMatch(preview -> preview.id().equals(postId));
    }

    private boolean isPostInPopularCache(
            Long postId, java.util.List<PostPreviewResponseDTO> cachedPopular) {
        return cachedPopular.stream().anyMatch(preview -> preview.id().equals(postId));
    }

    public void evictAllPostCaches(Post post) {
        log.info("Force evicting all caches for post type: {}", post.getType());

        // 상위 3페이지의 모든 캐시 무효화 (강제)
        for (int page = 0; page < MAX_CACHED_PAGES; page++) {
            cacheEvictionPublisher.publishEvict(
                    PostCacheKey.generatePostListKey(post.getType(), PostSort.RECENT, page));
            cacheEvictionPublisher.publishEvict(
                    PostCacheKey.generatePostListKey(post.getType(), PostSort.POPULAR, page));
        }

        // 인기 게시글 캐시 무효화
        cacheEvictionPublisher.publishEvict(PostCacheKey.POPULAR_POSTS);
    }
}
