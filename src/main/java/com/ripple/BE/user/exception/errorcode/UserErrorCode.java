package com.ripple.BE.user.exception.errorcode;

import com.ripple.BE.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "Image not found"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "Invalid Password"),
    INVALID_QUEST_TYPE(HttpStatus.BAD_REQUEST, "Invalid Quest Type"),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "Already exist Email"),
    QUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "Quest not found"),
    USER_GOAL_NOT_FOUND(HttpStatus.NOT_FOUND, "User Goal not found"),
    DUPLICATED_NICKNAME(
            HttpStatus.BAD_REQUEST, "Duplicated Nickname is already exist. Please use another nickname"),
    ATTENDANCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Attendance not found");

    private final HttpStatus httpStatus;
    private final String message;
}
