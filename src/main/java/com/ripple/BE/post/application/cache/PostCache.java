package com.ripple.BE.post.application.cache;

import com.ripple.BE.post.dto.response.PostPreviewListResponseDTO;
import com.ripple.BE.post.dto.response.ToktokPreviewListResponseDTO;

public interface PostCache {
    PostPreviewListResponseDTO getPostList(String key);

    void putPostList(String key, PostPreviewListResponseDTO value);

    ToktokPreviewListResponseDTO getToktokList(String key);

    void putToktokList(String key, ToktokPreviewListResponseDTO value);

    void evict(String key);

    void clear();
}
