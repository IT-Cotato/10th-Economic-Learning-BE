package com.ripple.BE.chatbot.dto.response;

import java.util.List;

import com.ripple.BE.chatbot.domain.ChatMessage;
import com.ripple.BE.chatbot.dto.ChatDTO;
import com.ripple.BE.chatbot.dto.ChatListDTO;
import com.ripple.BE.post.dto.response.PostPreviewResponse;

public record ChatListResponse(List<ChatResponse> postList, int totalPage, int currentPage) {

	public static ChatListResponse toChatListResponse(ChatListDTO chatListDTO) {
		return new ChatListResponse(
			chatListDTO.chatDTOList().stream().map(ChatResponse::toChatResponse).toList(),
			chatListDTO.totalPage(),
			chatListDTO.currentPage());
	}
