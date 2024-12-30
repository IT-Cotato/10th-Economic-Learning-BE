package com.ripple.BE.learning.exception.errorcode;

import com.ripple.BE.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LearningErrorCode implements ErrorCode {
    LEARNING_SET_NOT_FOUND(HttpStatus.NOT_FOUND, "Learning set not found"),
    SAVE_LEARNING_SET_EXCEL_FILE_FAILED(
            HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save learning set excel file"),
    QUIZ_PROGRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "Quiz progress not found");

    private final HttpStatus httpStatus;
    private final String message;
}
