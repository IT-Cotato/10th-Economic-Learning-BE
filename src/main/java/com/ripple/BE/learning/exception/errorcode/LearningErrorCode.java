package com.ripple.BE.learning.exception.errorcode;

import com.ripple.BE.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LearningErrorCode implements ErrorCode {

    // 학습 세트 관련 에러 코드
    LEARNING_SET_NOT_FOUND(HttpStatus.NOT_FOUND, "Learning set not found"),

    // 학습 세트 저장 에러 코드
    SAVE_LEARNING_SET_EXCEL_FILE_FAILED(
            HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save learning set excel file"),

    // 학습 세트 통계 관련 에러 코드
    LEARNING_SET_STAT_ILLEGAL_ARGUMENT(HttpStatus.BAD_REQUEST, "Learning set stat illegal argument"),

    // 개념 관련 에러 코드
    CONCEPT_NOT_FOUND(HttpStatus.NOT_FOUND, "Concept not found"),
    CONCEPT_ALREADY_SCRAP(HttpStatus.BAD_REQUEST, "Concept already scrap"),
    CONCEPT_SCRAP_NOT_FOUND(HttpStatus.NOT_FOUND, "Concept scrap not found"),

    // 퀴즈 관련 에러 코드
    QUIZ_PROGRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "Quiz progress not found"),
    QUIZ_NOT_FOUND(HttpStatus.NOT_FOUND, "Quiz not found"),
    QUIZ_SESSION_INTERNAL_SERVER_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR, "Quiz session internal server error"),
    QUIZ_ALREADY_SCRAP(HttpStatus.BAD_REQUEST, "Quiz already scrap"),
    QUIZ_SCRAP_NOT_FOUND(HttpStatus.NOT_FOUND, "Quiz scrap not found"),

    // 레벨 테스트 관련 에러 코드
    LEVEL_TEST_QUIZ_SESSION_EXPIRED(HttpStatus.BAD_REQUEST, "Level test quiz session expired"),
    LEVEL_TEST_QUIZ_NOT_FOUND(HttpStatus.NOT_FOUND, "Level test quiz not found");

    private final HttpStatus httpStatus;
    private final String message;
}
