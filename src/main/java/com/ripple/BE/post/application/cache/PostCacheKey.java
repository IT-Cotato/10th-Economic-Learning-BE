package com.ripple.BE.post.application.cache;

import com.ripple.BE.post.domain.type.PostSort;
import com.ripple.BE.post.domain.type.PostType;

public class PostCacheKey {
    public static final String POPULAR_POSTS = "post::popular";
    public static final String POST_LIST_PREFIX = "post::";
    public static final String TOKTOK_LIST_PREFIX = "toktok::";
    public static final String POST_EVICT_ALL = "post::evictAll";
    public static final String TOKTOK_EVICT_ALL = "toktok::evictAll";

    public static String generatePostListKey(PostType type, PostSort sort, int page) {
        return POST_LIST_PREFIX
                + (type != null ? type.name() : "ALL")
                + "::"
                + sort.name()
                + "::"
                + page;
    }

    public static String generateToktokListKey(PostSort sort, int page) {
        return TOKTOK_LIST_PREFIX + sort.name() + "::" + page;
    }
}
