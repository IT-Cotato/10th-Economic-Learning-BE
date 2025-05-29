package com.ripple.BE.chatbot.exception.errorcode;

import com.ripple.BE.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatbotErrorCode implements ErrorCode {
    CHATBOT_NOT_FOUND(HttpStatus.NOT_FOUND, "Chatbot not found");

    private final HttpStatus httpStatus;
    private final String message;
}
