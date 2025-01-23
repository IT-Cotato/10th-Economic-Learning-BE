package com.ripple.BE.learning.exception;

import com.ripple.BE.learning.exception.errorcode.QuizErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class QuizException extends RuntimeException {

    private final QuizErrorCode errorCode;
}
