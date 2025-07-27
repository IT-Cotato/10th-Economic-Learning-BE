package com.ripple.BE.notification.application.sse;

import com.ripple.BE.notification.dto.NotificationSseDTO;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/** SSE 연결을 관리하고 알림 및 하트비트를 처리하는 컴포넌트 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SseEmitterManager {

    private final ThreadPoolTaskScheduler heartbeatScheduler; // 하트비트 전송용 스케줄러
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor; // 비동기 작업 실행용

    // 클라이언트 ID 기준 emitter 관리
    private final Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>(512);
    private final Map<String, ScheduledFuture<?>> heartbeatMap = new ConcurrentHashMap<>(512);
    private final Map<String, Boolean> emitterClosedMap = new ConcurrentHashMap<>(512);

    /** SSE 구독 요청 처리 */
    public SseEmitter connect(String clientId) {
        SseEmitter emitter = createEmitter(clientId);

        try {
            // 연결 성공 메시지 전송
            emitter.send(
                    SseEmitter.event()
                            .id(UUID.randomUUID().toString())
                            .name("connect")
                            .data("connected")
                            .reconnectTime(45000)); // 45초 후 재연결 시도

            // 하트비트 등록
            startHeartbeat(clientId);

            // emitter 및 상태 저장
            emitterMap.put(clientId, emitter);
            emitterClosedMap.put(clientId, false);

        } catch (IOException e) {
            log.error("초기 연결 실패: {}", clientId, e);
            closeEmitter(clientId, e);
        }

        return emitter;
    }

    /** Emitter 생성 및 콜백 등록 */
    private SseEmitter createEmitter(String clientId) {
        SseEmitter emitter = new SseEmitter(TimeUnit.MINUTES.toMillis(30));

        emitter.onCompletion(() -> closeEmitter(clientId, null));
        emitter.onTimeout(() -> closeEmitter(clientId, new TimeoutException("Emitter timeout")));
        emitter.onError(e -> closeEmitter(clientId, e));

        return emitter;
    }

    /** 하트비트 주기적으로 전송 시작 */
    private void startHeartbeat(String clientId) {
        ScheduledFuture<?> heartbeat =
                heartbeatScheduler.scheduleAtFixedRate(
                        () -> sendHeartbeat(clientId), Duration.ofSeconds(30));
        heartbeatMap.put(clientId, heartbeat);
    }

    /** 하트비트 전송 및 연결 상태 체크 */
    private void sendHeartbeat(String clientId) {
        emitterClosedMap.computeIfAbsent(clientId, k -> false);

        if (!emitterMap.containsKey(clientId) || Boolean.TRUE.equals(emitterClosedMap.get(clientId))) {
            stopHeartbeat(clientId);
            return;
        }

        try {
            emitterMap
                    .get(clientId)
                    .send(
                            SseEmitter.event()
                                    .id(UUID.randomUUID().toString())
                                    .name("heartbeat")
                                    .comment("Total connections: " + emitterMap.size())
                                    .data("keep-alive"));
        } catch (Exception e) {
            log.warn("하트비트 실패: {}", clientId);
            closeEmitter(clientId, e);
        }
    }

    /** 연결 종료 처리 */
    private void closeEmitter(String clientId, Throwable cause) {
        if (Boolean.TRUE.equals(emitterClosedMap.getOrDefault(clientId, false))) {
            return;
        }

        emitterClosedMap.put(clientId, true);
        SseEmitter emitter = emitterMap.remove(clientId);
        stopHeartbeat(clientId);

        if (emitter == null) {
            return;
        }

        threadPoolTaskExecutor.submit(
                () -> {
                    try {
                        if (cause != null) {
                            log.info("Emitter 종료: {}, 원인: {}", clientId, cause.getMessage());
                        }
                        emitter.complete();
                    } catch (Exception e) {
                        log.info("Emitter 종료 중 오류 발생: {}", clientId, e);
                    }
                });
    }

    /** 하트비트 정지 */
    private void stopHeartbeat(String clientId) {
        ScheduledFuture<?> task = heartbeatMap.remove(clientId);
        if (task != null) task.cancel(true);
    }

    /** 알림 전송 */
    public void pushNotification(String clientId, NotificationSseDTO notification) {

        threadPoolTaskExecutor.submit(
                () -> {
                    SseEmitter emitter = emitterMap.get(clientId);
                    if (emitter != null) {
                        try {
                            emitter.send(
                                    SseEmitter.event()
                                            .id(UUID.randomUUID().toString())
                                            .name("notification")
                                            .data(notification));
                        } catch (IOException e) {
                            log.error("알림 전송 실패: {}", clientId, e);
                            closeEmitter(clientId, e);
                        }
                    }
                });
    }

    /** 사용자의 Emiiter 닫기 */
    public void closeEmitter(String clientId) {
        if (Boolean.TRUE.equals(emitterClosedMap.getOrDefault(clientId, false))) {
            return;
        }

        emitterClosedMap.put(clientId, true);
        stopHeartbeat(clientId);
        SseEmitter emitter = emitterMap.remove(clientId);

        if (emitter != null) {
            try {
                emitter.complete();
            } catch (Exception e) {
                log.warn("Emitter 종료 중 예외 발생: {}", clientId, e);
            }
        } else {
            log.warn("Emitter가 존재하지 않습니다: {}", clientId);
        }
    }

    public boolean isConnected(String clientId) {
        return !Boolean.TRUE.equals(emitterClosedMap.getOrDefault(clientId, false));
    }
}
