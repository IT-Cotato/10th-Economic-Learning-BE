package com.ripple.BE.notification.dto;

import com.ripple.BE.notification.domain.Notification;
import java.util.List;

public record NotificationListDTO(List<NotificationDTO> notificationDTOList) {

    public static NotificationListDTO toNotificationListDTO(
            final List<Notification> notificationList) {
        return new NotificationListDTO(
                notificationList.stream().map(NotificationDTO::toNotificationDTO).toList());
    }
}
