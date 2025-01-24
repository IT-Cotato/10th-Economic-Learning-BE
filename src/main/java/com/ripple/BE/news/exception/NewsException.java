package com.ripple.BE.news.exception;

import com.ripple.BE.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NewsException extends RuntimeException {

    private final ErrorCode errorCode;
}
