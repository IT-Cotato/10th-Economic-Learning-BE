package com.ripple.BE.image.exception.errorcode;

import com.ripple.BE.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ImageErrorCode implements ErrorCode {
    IMAGE_PROCESSING_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "Image processing fail"),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "Image not found");

    private final HttpStatus httpStatus;
    private final String message;
}
