package com.ripple.BE.post.exception.errorcode;

import com.ripple.BE.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostErrorCode implements ErrorCode {
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "Post not found"),
    TOKTOK_POST_NOT_FOUND(HttpStatus.NOT_FOUND, "Toktok post not found"),
    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "Like not found"),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Comment not found"),
    SCRAP_NOT_FOUND(HttpStatus.NOT_FOUND, "Scrap not found"),
    TOKTOK_NOT_FOUND(HttpStatus.NOT_FOUND, "TodayToktok not found"),

    LIKE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "Like already exists"),
    SCRAP_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "Scrap already exists"),

    POST_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "Post not authorized"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
