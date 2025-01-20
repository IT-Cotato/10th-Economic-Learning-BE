package com.ripple.BE.image.exception;

import com.ripple.BE.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ImageException extends RuntimeException {

    private final ErrorCode errorCode;
}
