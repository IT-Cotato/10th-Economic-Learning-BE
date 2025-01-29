package com.ripple.BE.notification.controller;

import com.ripple.BE.global.dto.response.ApiResponse;
import com.ripple.BE.notification.dto.NotificationListDTO;
import com.ripple.BE.notification.dto.response.NotificationListResponse;
import com.ripple.BE.notification.dto.response.UnreadCountResponse;
import com.ripple.BE.notification.service.NotificationService;
import com.ripple.BE.notification.service.SseEmitterManager;
import com.ripple.BE.user.domain.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/notification")
@Tag(name = "Notification", description = "알림 관련 API")
public class NotificationController {

    private final NotificationService notificationService;
    private final SseEmitterManager sseEmitterManager;

    @Operation(
            summary = "알림 구독",
            description =
                    "알림 구독을 위한 SseEmitter 생성, 푸시 알림을 받기 전에 이 API를 호출해야 합니다. 연결 종료로 인해 푸시 알림을 받지 못한 경우, Last-Event-ID 헤더를 통해 마지막으로 수신한 이벤트 ID를 전달하여 누락된 이벤트를 재전송받을 수 있습니다.")
    @GetMapping(path = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @RequestHeader(name = "Last-Event-ID", required = false) String lastEventId) {

        return sseEmitterManager.subscribe(currentUser.getId(), lastEventId);
    }

    @Operation(summary = "알림 삭제", description = "알림을 삭제합니다.")
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<ApiResponse<Object>> deleteNotification(
            final @PathVariable("notificationId") long notificationId) {

        notificationService.deleteNotification(notificationId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.EMPTY_RESPONSE);
    }

    @Operation(summary = "알림 조회", description = "알림을 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getNotificationList(
            final @AuthenticationPrincipal CustomUserDetails currentUser) {

        NotificationListDTO notificationListDTO =
                notificationService.getNotifications(currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.from(
                                NotificationListResponse.toNotificationListResponse(notificationListDTO)));
    }

    @Operation(summary = "알림 확인", description = "알림을 확인합니다.")
    @PostMapping("/{notificationId}/check")
    public ResponseEntity<ApiResponse<Object>> checkNotification(
            final @PathVariable("notificationId") long notificationId) {

        notificationService.markAsRead(notificationId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.EMPTY_RESPONSE);
    }

    @Operation(summary = "읽지 않은 알림 개수 조회", description = "읽지 않은 알림 개수를 조회합니다.")
    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponse<Object>> getUnreadNotificationCount(
            final @AuthenticationPrincipal CustomUserDetails currentUser) {

        long unreadNotificationCount =
                notificationService.getUnreadNotificationCount(currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(UnreadCountResponse.toUnreadCountResponse(unreadNotificationCount)));
    }
}
