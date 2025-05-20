package com.ripple.BE.global.config.cache;

import static com.ripple.BE.global.config.cache.PostCacheKeyGenerator.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration defaultConfig =
                RedisCacheConfiguration.defaultCacheConfig()
                        .disableCachingNullValues()
                        .entryTtl(Duration.ofMinutes(5)) // 기본 TTL
                        .computePrefixWith(CacheKeyPrefix.simple())
                        .serializeKeysWith(
                                RedisSerializationContext.SerializationPair.fromSerializer(
                                        new StringRedisSerializer()))
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair.fromSerializer(
                                        new GenericJackson2JsonRedisSerializer()));

        // 캐시 이름별 TTL 설정
        Map<String, RedisCacheConfiguration> cacheConfigs = new HashMap<>();
        cacheConfigs.put(
                CACHE_NAME_POSTS, defaultConfig.entryTtl(Duration.ofSeconds(15))); // 게시글 목록 캐시 15초
        cacheConfigs.put(
                CACHE_NAME_POPULAR_POSTS, defaultConfig.entryTtl(Duration.ofMinutes(5))); // 인기 게시글 캐시 5분
        cacheConfigs.put(
                CACHE_NAME_POST_SEARCH, defaultConfig.entryTtl(Duration.ofSeconds(30))); // 게시글 검색 캐시 30초
        cacheConfigs.put(
                CACHE_NAME_TOKTOK_SEARCH,
                defaultConfig.entryTtl(Duration.ofSeconds(30))); // Toktok 검색 캐시 30초

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigs)
                .build();
    }
}
