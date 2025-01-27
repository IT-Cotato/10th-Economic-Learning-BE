package com.ripple.BE.chatbot.service;

import com.ripple.BE.chatbot.domain.ChatMessage;
import com.ripple.BE.chatbot.domain.type.Sender;
import com.ripple.BE.chatbot.dto.ChatDTO;
import com.ripple.BE.chatbot.dto.response.ChatResponse;
import com.ripple.BE.chatbot.repository.ChatbotRepository;
import com.ripple.BE.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ChatbotService {
    private final OpenAiChatClient openAiChatClient;

    private final UserRepository userRepository;
    private final ChatbotRepository chatbotRepository;

    @Transactional
    public ChatResponse sendMessage(final ChatDTO chatDTO, final Long userId) {
        String response = openAiChatClient.call(chatDTO.message());

        // 유저의 메세지 저장
        chatbotRepository.save(
                ChatMessage.builder()
                        .user(userRepository.findById(userId).get())
                        .message(chatDTO.message())
                        .sender(Sender.USER)
                        .build());

        // 챗봇의 응답 저장
        chatbotRepository.save(
                ChatMessage.builder()
                        .user(userRepository.findById(userId).get())
                        .message(response)
                        .sender(Sender.CHATBOT)
                        .build());

        return new ChatResponse(response);
    }
}
