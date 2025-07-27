package com.ripple.BE.notification.application.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ripple.BE.notification.application.sse.SseEmitterManager;
import com.ripple.BE.notification.dto.NotificationSseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisNotificationSubscriber {

    private final ObjectMapper objectMapper;
    private final SseEmitterManager sseEmitterManager;

    /** Redis에서 알림 메시지가 발행(publish)되면 실행 */
    public void onNotification(String message) {
        try {
            // 받은 JSON 문자열을 NotificationDto 객체로 변환
            NotificationSseDTO notificationSseDTO =
                    objectMapper.readValue(message, NotificationSseDTO.class);
            log.info("Received notification from Redis: {}", notificationSseDTO);

            // SseEmitterManager를 통해 해당 알림을 구독 중인 클라이언트에게 전송
            String receiverId = notificationSseDTO.receiverId().toString();

            // EmitterManager가 emitter 상태를 체크하도록 수정
            if (!sseEmitterManager.isConnected(receiverId)) {
                return;
            }

            sseEmitterManager.pushNotification(receiverId, notificationSseDTO);

        } catch (Exception e) {
            log.error("Error processing notification message: {}", message, e);
        }
    }
}
