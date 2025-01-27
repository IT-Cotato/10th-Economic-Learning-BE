package com.ripple.BE.chatbot.dto;

import com.ripple.BE.chatbot.domain.ChatMessage;
import com.ripple.BE.chatbot.domain.type.Sender;
import com.ripple.BE.chatbot.dto.request.ChatRequest;

public record ChatDTO(String message, Sender sender) {

    public static ChatDTO toChatDTO(final ChatMessage chatMessage) {
        return new ChatDTO(chatMessage.getMessage(), chatMessage.getSender());
    }

    public static ChatDTO tochatDTO(final ChatRequest request) {
        return new ChatDTO(request.message(), Sender.USER);
    }
}
