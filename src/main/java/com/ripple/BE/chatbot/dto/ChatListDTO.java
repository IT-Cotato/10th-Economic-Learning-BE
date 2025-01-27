package com.ripple.BE.chatbot.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ripple.BE.chatbot.domain.ChatMessage;
import com.ripple.BE.post.domain.Post;
import com.ripple.BE.post.dto.PostDTO;
import com.ripple.BE.post.dto.PostListDTO;

import lombok.Builder;

@Builder
public record ChatListDTO (
	List<ChatDTO> chatDTOList, int totalPage, int currentPage
) {
	public static ChatListDTO toChatListDTO(Page<ChatMessage> chatMessagePage) {
		return new ChatListDTO(
			chatMessagePage.getContent().stream().map(ChatDTO::toChatDTO).toList(),
			chatMessagePage.getTotalPages(),
			chatMessagePage.getNumber());
	}
}
