package com.ripple.BE.notification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ripple.BE.notification.dto.NotificationDTO;
import com.ripple.BE.notification.repository.EmitterRepository;
import com.ripple.BE.user.domain.User;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
@RequiredArgsConstructor
@Slf4j
public class SseEmitterManager {

    private final EmitterRepository emitterRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SseEmitter subscribe(final long userId, final String lastEventId) {

        String emitterId = userId + "_" + System.currentTimeMillis();

        SseEmitter sseEmitter = emitterRepository.save(emitterId, new SseEmitter(Long.MAX_VALUE));

        // 상황별 emitter 삭제 처리
        sseEmitter.onCompletion(() -> emitterRepository.deleteEmitterById(emitterId)); // 연결 종료 시
        sseEmitter.onTimeout(() -> emitterRepository.deleteEmitterById(emitterId)); // 타임아웃 시
        sseEmitter.onError((e) -> emitterRepository.deleteEmitterById(emitterId)); // 에러 발생 시

        // 503 Service Unavailable 방지용 dummy event 전송
        sendDummyEvent(sseEmitter, emitterId);

        // client가 미수신한 event 목록이 존재하는 경우
        if (lastEventId != null && !lastEventId.isEmpty()) {
            Map<String, Object> eventCaches = emitterRepository.findAllEventCacheByUserId(userId);
            eventCaches.entrySet().stream() // 미수신 상태인 event 목록 전송
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendEventToClient(sseEmitter, entry.getKey(), entry.getValue()));
        }

        return sseEmitter;
    }

    public void send(User receiver, NotificationDTO data) {
        String emitterId = null;

        try {
            Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterByUserId(receiver.getId());

            if (emitters.isEmpty()) {
                return;
            }

            emitterId = emitters.keySet().iterator().next();

            emitters.forEach(
                    (key, value) -> {
                        emitterRepository.saveEventCache(key, data);
                        sendEventToClient(value, key, data);
                    });
        } catch (Exception e) {
            emitterRepository.deleteEmitterById(emitterId);
        }
    }

    private void sendEventToClient(SseEmitter sseEmitter, String emitterId, Object data) {
        try {
            String jsonData = objectMapper.writeValueAsString(data);
            sseEmitter.send(
                    SseEmitter.event()
                            .id(emitterId)
                            .name("notification")
                            .data(jsonData, MediaType.APPLICATION_JSON));
        } catch (IOException e) {
            emitterRepository.deleteEmitterById(emitterId);
            sseEmitter.completeWithError(e);
        }
    }

    private void sendDummyEvent(SseEmitter sseEmitter, String emitterId) {
        try {
            sseEmitter.send(
                    SseEmitter.event()
                            .id(emitterId)
                            .name("test")
                            .data("Connection established", MediaType.APPLICATION_JSON));
        } catch (IOException e) {
            emitterRepository.deleteEmitterById(emitterId);
            sseEmitter.completeWithError(e);
        }
    }

    // 알림 삭제
    public void deleteEmitter(final long userId) {
        emitterRepository.deleteAllEmitterById(userId);
        emitterRepository.deleteAllEventCacheById(userId);
    }
}
