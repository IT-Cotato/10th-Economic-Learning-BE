package com.ripple.BE.term.exception.errorcode;

import com.ripple.BE.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TermErrorCode implements ErrorCode {
    TERM_NOT_FOUND(HttpStatus.NOT_FOUND, "Term not found"),
    TERM_SCRAP_NOT_FOUND(HttpStatus.NOT_FOUND, "Term scrap not found"),

    SAVE_TERM_EXCEL_FILE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save term excel file"),
    TERM_SCRAP_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "Term scrap already exist");

    private final HttpStatus httpStatus;
    private final String message;
}
