package com.ripple.BE.notification.dto;

import com.ripple.BE.notification.domain.Notification;
import com.ripple.BE.notification.domain.NotificationType;

public record NotificationSseDTO(
        Long receiverId, String title, String content, NotificationType type) {

    public static NotificationSseDTO from(Notification notification) {
        return new NotificationSseDTO(
                notification.getReceiverId(),
                notification.getTitle(),
                notification.getContent(),
                notification.getType());
    }
}
