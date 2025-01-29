package com.ripple.BE.notification.dto.response;

import com.ripple.BE.global.utils.RelativeTimeFormatter;
import com.ripple.BE.notification.domain.NotificationType;
import com.ripple.BE.notification.dto.NotificationDTO;

public record NotificationResponse(
        Long id,
        String postTitle,
        String content,
        boolean isRead,
        NotificationType type,
        long postId,
        String createdDate) {

    public static NotificationResponse toNotificationResponse(NotificationDTO notificationDTO) {
        return new NotificationResponse(
                notificationDTO.id(),
                notificationDTO.title(),
                notificationDTO.content(),
                notificationDTO.isRead(),
                notificationDTO.type(),
                notificationDTO.postId(),
                RelativeTimeFormatter.formatRelativeTime(notificationDTO.createdDate()));
    }
}
