package com.ripple.BE.user.exception.errorcode;

import com.ripple.BE.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "Invalid Password"),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "Already exist Email"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
