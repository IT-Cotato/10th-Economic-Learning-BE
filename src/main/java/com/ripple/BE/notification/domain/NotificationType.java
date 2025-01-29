package com.ripple.BE.notification.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {
    COMMENT("댓글"),
    REPLY("대댓글"),
    POPULAR("인기글");

    private final String notificationType;
}
