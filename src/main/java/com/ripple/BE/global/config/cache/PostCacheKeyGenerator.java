package com.ripple.BE.global.config.cache;

import java.lang.reflect.Method;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

@Component(PostCacheKeyGenerator.POST_CACHE_KEY_GENERATOR)
public class PostCacheKeyGenerator implements KeyGenerator {

    public static final String POST_CACHE_KEY_GENERATOR = "postCacheKeyGenerator";

    public static final String CACHE_NAME_POSTS = "posts"; // 게시글 목록 조회
    public static final String CACHE_NAME_POPULAR_POSTS = "popularPosts"; // 인기 게시글
    public static final String CACHE_NAME_POST_SEARCH = "postSearch"; // 게시글 검색

    public static final String CACHE_NAME_TOKTOK_SEARCH = "toktokSearch"; // 오늘의 톡톡

    @Override
    public Object generate(Object target, Method method, Object... params) {
        StringBuilder key = new StringBuilder();
        key.append(method.getName());

        if (params.length > 0) {
            key.append(":");
            for (Object param : params) {
                if (param == null) {
                    key.append("null"); // 진짜 null
                } else if ("null".equals(param.toString())) {
                    key.append("str_null"); // 문자열 "null"은 접두어 붙이기
                } else {
                    key.append(param);
                }
                key.append(":");
            }
            key.deleteCharAt(key.length() - 1);
        }

        return key.toString();
    }
}
