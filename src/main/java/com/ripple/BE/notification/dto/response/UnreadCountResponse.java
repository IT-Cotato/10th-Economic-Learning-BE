package com.ripple.BE.notification.dto.response;

public record UnreadCountResponse(long unreadCount) {
    public static UnreadCountResponse toUnreadCountResponse(final long unreadCount) {
        return new UnreadCountResponse(unreadCount);
    }
}
