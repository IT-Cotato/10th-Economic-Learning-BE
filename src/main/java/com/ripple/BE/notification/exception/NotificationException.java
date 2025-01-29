package com.ripple.BE.notification.exception;

import com.ripple.BE.notification.exception.errorcode.NotificationErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotificationException extends RuntimeException {

    private final NotificationErrorCode errorCode;
}
