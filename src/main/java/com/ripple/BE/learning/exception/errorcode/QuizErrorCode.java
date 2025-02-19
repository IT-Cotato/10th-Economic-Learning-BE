package com.ripple.BE.learning.exception.errorcode;

import com.ripple.BE.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum QuizErrorCode implements ErrorCode {
    QUIZ_NOT_FOUND(HttpStatus.NOT_FOUND, "Quiz not found"),
    QUIZ_ALREADY_SCRAP(HttpStatus.BAD_REQUEST, "Quiz already scrap"),
    QUIZ_SCRAP_NOT_FOUND(HttpStatus.NOT_FOUND, "Quiz scrap not found"),
    QUIZ_EXPIRED(HttpStatus.BAD_REQUEST, "Quiz expired");

    private final HttpStatus httpStatus;
    private final String message;
}
