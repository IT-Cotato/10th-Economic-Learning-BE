package com.ripple.BE.global.config;

import com.ripple.BE.notification.application.redis.RedisNotificationSubscriber;
import com.ripple.BE.post.application.cache.redis.PostCacheEvictionSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisListenerConfig {

    private final RedisNotificationSubscriber notificationSubscriber;
    private final PostCacheEvictionSubscriber postCacheEvictionSubscriber;

    /** 알림용 Redis 채널 토픽 빈 설정 */
    @Bean
    public ChannelTopic notificationTopic() {
        return new ChannelTopic("notification");
    }

    /** 캐시 무효화를 위한 Redis 채널 토픽 빈 설정 */
    @Bean
    public ChannelTopic postCacheEvictTopic() {
        return new ChannelTopic("post-cache-evict");
    }

    /** 알림 메시지를 처리하는 subscriber 설정 추가 */
    @Bean
    public MessageListenerAdapter listenerAdapterNotification() {
        return new MessageListenerAdapter(notificationSubscriber, "onNotification");
    }

    @Bean
    public MessageListenerAdapter listenerAdapterCacheEvict() {
        return new MessageListenerAdapter(postCacheEvictionSubscriber, "onEvictMessage");
    }

    /** redis 에 발행(publish)된 메시지 처리를 위한 리스너 컨테이너 설정 */
    @Bean
    public RedisMessageListenerContainer redisMessageListener(
            RedisConnectionFactory redisConnectionFactory,
            MessageListenerAdapter listenerAdapterNotification,
            ChannelTopic notificationTopic // 알림 토픽
            ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);

        // notificationTopic에 대한 메시지 리스너 어댑터 설정
        container.addMessageListener(listenerAdapterNotification, notificationTopic);

        // postCacheEvictTopic에 대한 메시지 리스너 어댑터 설정
        container.addMessageListener(listenerAdapterCacheEvict(), postCacheEvictTopic());

        return container;
    }
}
