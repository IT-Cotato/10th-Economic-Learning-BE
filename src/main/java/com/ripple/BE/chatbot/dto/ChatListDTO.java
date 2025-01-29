package com.ripple.BE.chatbot.dto;

import com.ripple.BE.chatbot.domain.ChatMessage;
import java.util.List;
import lombok.Builder;
import org.springframework.data.domain.Page;

@Builder
public record ChatListDTO(List<ChatDTO> chatDTOList, int totalPage, int currentPage) {
    public static ChatListDTO toChatListDTO(Page<ChatMessage> chatMessagePage) {
        return new ChatListDTO(
                chatMessagePage.getContent().stream().map(ChatDTO::toChatDTO).toList(),
                chatMessagePage.getTotalPages(),
                chatMessagePage.getNumber());
    }
}
