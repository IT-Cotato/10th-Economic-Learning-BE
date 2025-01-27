package com.ripple.BE.chatbot.dto.response;

import com.ripple.BE.chatbot.domain.ChatMessage;
import com.ripple.BE.chatbot.dto.ChatDTO;

public record ChatResponse(
	String message,
	String sender) {

	public static ChatResponse toChatResponse(ChatDTO chatDTO) {
		return new ChatResponse(chatDTO.message(), chatDTO.sender().toString());
	}
}

