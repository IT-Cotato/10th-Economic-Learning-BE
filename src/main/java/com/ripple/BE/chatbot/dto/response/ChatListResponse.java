package com.ripple.BE.chatbot.dto.response;

import com.ripple.BE.chatbot.dto.ChatListDTO;
import java.util.List;

public record ChatListResponse(List<ChatResponse> chatResponses, int totalPage, int currentPage) {

    public static ChatListResponse toChatListResponse(ChatListDTO chatListDTO) {
        return new ChatListResponse(
                chatListDTO.chatDTOList().stream().map(ChatResponse::toChatResponse).toList(),
                chatListDTO.totalPage(),
                chatListDTO.currentPage());
    }
}
