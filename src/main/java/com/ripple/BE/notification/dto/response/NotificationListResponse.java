package com.ripple.BE.notification.dto.response;

import com.ripple.BE.notification.dto.NotificationListDTO;
import java.util.List;

public record NotificationListResponse(List<NotificationResponse> notificationResponseList) {

    public static NotificationListResponse toNotificationListResponse(
            final NotificationListDTO notificationListDTO) {
        return new NotificationListResponse(
                notificationListDTO.notificationDTOList().stream()
                        .map(NotificationResponse::toNotificationResponse)
                        .toList());
    }
}
