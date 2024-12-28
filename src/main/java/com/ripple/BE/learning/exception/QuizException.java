package com.ripple.BE.learning.exception;

import com.ripple.BE.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class QuizException extends RuntimeException {

    private final ErrorCode errorCode;
}
