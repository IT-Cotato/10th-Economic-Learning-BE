package com.ripple.BE.notification.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ripple.BE.notification.domain.Notification;
import com.ripple.BE.notification.domain.NotificationType;
import java.time.LocalDateTime;

public record NotificationDTO(
        Long id,
        String title,
        String content,
        boolean isRead,
        NotificationType type,
        long postId,
        @JsonSerialize(using = LocalDateTimeSerializer.class)
                @JsonDeserialize(using = LocalDateTimeDeserializer.class)
                LocalDateTime createdDate) {

    public static NotificationDTO toNotificationDTO(final Notification notification) {
        return new NotificationDTO(
                notification.getId(),
                notification.getTitle(),
                notification.getContent(),
                notification.isRead(),
                notification.getType(),
                notification.getPost().getId(),
                notification.getCreatedDate());
    }
}
