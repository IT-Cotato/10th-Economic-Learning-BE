package com.ripple.BE.notification.dto.response;

import com.ripple.BE.global.utils.RelativeTimeFormatter;
import com.ripple.BE.notification.domain.Notification;
import com.ripple.BE.notification.domain.NotificationType;

public record NotificationResponseDTO(
        Long id,
        String postTitle,
        String content,
        boolean isRead,
        NotificationType type,
        long postId,
        String createdDate) {

    public static NotificationResponseDTO from(Notification notification) {
        return new NotificationResponseDTO(
                notification.getId(),
                notification.getTitle(),
                notification.getContent(),
                notification.isRead(),
                notification.getType(),
                notification.getPostId(),
                RelativeTimeFormatter.formatRelativeTime(notification.getCreatedDate()));
    }
}
