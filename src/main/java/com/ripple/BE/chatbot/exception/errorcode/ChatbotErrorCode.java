package com.ripple.BE.chatbot.exception.errorcode;

import com.ripple.BE.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatbotErrorCode implements ErrorCode {
    OPEN_API_RESPONSE_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "OpenAi 에서 예외 응답 받음"),
    OPEN_AI_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "OpenAI 호출 실패");

    private final HttpStatus httpStatus;
    private final String message;
}
