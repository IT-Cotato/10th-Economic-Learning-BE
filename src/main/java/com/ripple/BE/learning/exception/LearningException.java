package com.ripple.BE.learning.exception;

import com.ripple.BE.learning.exception.errorcode.LearningErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LearningException extends RuntimeException {

    private final LearningErrorCode errorCode;
}
