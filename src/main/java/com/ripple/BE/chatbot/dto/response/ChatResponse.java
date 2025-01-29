package com.ripple.BE.chatbot.dto.response;

import com.ripple.BE.chatbot.domain.ChatMessage;
import com.ripple.BE.chatbot.dto.ChatDTO;

public record ChatResponse(String message, String sender, String createdAt) {

    public static ChatResponse toChatResponse(ChatDTO chatDTO) {
        return new ChatResponse(
                chatDTO.message(), chatDTO.sender().toString(), chatDTO.createdAt().toString());
    }

    public static ChatResponse toChatResponse(ChatMessage chatMessage) {
        return new ChatResponse(
                chatMessage.getMessage(),
                chatMessage.getSender().toString(),
                chatMessage.getCreatedDate().toString());
    }
}
