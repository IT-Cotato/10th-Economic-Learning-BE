package com.ripple.BE.chatbot.exception;

import com.ripple.BE.chatbot.exception.errorcode.ChatbotErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatbotException extends RuntimeException {
    private final ChatbotErrorCode errorCode;
}
