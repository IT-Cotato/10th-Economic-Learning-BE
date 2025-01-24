package com.ripple.BE.term.exception;

import com.ripple.BE.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TermException extends RuntimeException {

    private final ErrorCode errorCode;
}
