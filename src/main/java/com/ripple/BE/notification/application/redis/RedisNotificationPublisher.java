package com.ripple.BE.notification.application.redis;

import com.ripple.BE.notification.dto.NotificationSseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisNotificationPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic notificationTopic;

    /** Redis를 사용하여 알림을 발행 */
    public void publishNotification(NotificationSseDTO notificationSseDTO) {
        log.info("Publishing notification to Redis topic: {}", notificationTopic.getTopic());

        // Redis에 알림 DTO를 발행
        redisTemplate.convertAndSend(notificationTopic.getTopic(), notificationSseDTO);
    }
}
