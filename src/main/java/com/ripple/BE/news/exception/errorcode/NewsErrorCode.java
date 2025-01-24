package com.ripple.BE.news.exception.errorcode;

import com.ripple.BE.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NewsErrorCode implements ErrorCode {
    NEWS_NOT_FOUND(HttpStatus.NOT_FOUND, "News not found"),
    NEWS_SCRAP_NOT_FOUND(HttpStatus.NOT_FOUND, "News scrap not found"),
    NEWS_SCRAP_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "News scrap already exist"),
    NEWS_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
