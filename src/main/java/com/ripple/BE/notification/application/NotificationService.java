package com.ripple.BE.notification.application;

import static com.ripple.BE.notification.exception.errorcode.NotificationErrorCode.*;

import com.ripple.BE.notification.domain.Notification;
import com.ripple.BE.notification.dto.response.NotificationResponseDTO;
import com.ripple.BE.notification.exception.NotificationException;
import com.ripple.BE.notification.persistence.NotificationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;

    // 사용자 알림 목록 조회
    public List<NotificationResponseDTO> getNotifications(final long userId) {

        return notificationRepository.findByUserIdOrderByCreatedDateDesc(userId).stream()
                .map(NotificationResponseDTO::from)
                .toList();
    }

    // 읽지 않은 알림 개수 조회
    public long getUnreadNotificationCount(final long receiverId) {
        return notificationRepository.countByUserIdAndIsReadFalse(receiverId);
    }

    // 알림 읽음 처리
    @Transactional
    public void markAsRead(final long notificationId) {
        Notification notification =
                notificationRepository
                        .findById(notificationId)
                        .orElseThrow(() -> new NotificationException(NOTIFICATION_NOT_FOUND));

        notification = notification.updateIsRead(true);
        notificationRepository.save(notification);
    }

    // 알림 삭제
    @Transactional
    public void deleteNotification(final long notificationId) {
        Notification notification =
                notificationRepository
                        .findById(notificationId)
                        .orElseThrow(() -> new NotificationException(NOTIFICATION_NOT_FOUND));

        notificationRepository.delete(notification);
    }
}
