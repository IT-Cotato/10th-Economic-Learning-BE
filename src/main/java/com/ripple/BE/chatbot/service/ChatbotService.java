package com.ripple.BE.chatbot.service;

import static com.ripple.BE.user.exception.errorcode.UserErrorCode.*;

import com.ripple.BE.chatbot.domain.ChatMessage;
import com.ripple.BE.chatbot.domain.type.Sender;
import com.ripple.BE.chatbot.dto.ChatDTO;
import com.ripple.BE.chatbot.dto.ChatListDTO;
import com.ripple.BE.chatbot.dto.response.ChatResponse;
import com.ripple.BE.chatbot.repository.ChatbotRepository;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.exception.UserException;
import com.ripple.BE.user.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
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
    private final OpenAiClient openAiClient;

    private static final int PAGE_SIZE = 10;

    @Transactional
    public ChatResponse sendMessage(final ChatDTO chatDTO, final Long userId) {
        User user =
                userRepository.findById(userId).orElseThrow(() -> new UserException(USER_NOT_FOUND));

        // 프롬프트 포함하여 OpenAI API 호출
        String responseFromOpenAi = openAiClient.getResponseFromOpenAi(chatDTO.message());

        // 유저의 메세지 저장
        chatbotRepository.save(
                ChatMessage.builder().user(user).message(responseFromOpenAi).sender(Sender.USER).build());

        // 챗봇의 응답 저장
        ChatMessage saved =
                chatbotRepository.save(
                        ChatMessage.builder()
                                .user(user)
                                .message(responseFromOpenAi)
                                .sender(Sender.CHATBOT)
                                .build());

        return ChatResponse.toChatResponse(saved);
    }

    public ChatListDTO getChatList(final Long userId, final int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        User user =
                userRepository.findById(userId).orElseThrow(() -> new UserException(USER_NOT_FOUND));

        Page<ChatMessage> chatMessagePage =
                chatbotRepository.findAllByUserIdOrderByCreatedDateDesc(user.getId(), pageable);

        return ChatListDTO.toChatListDTO(chatMessagePage);
    }

    @Transactional
    public void clearChat(final Long userId) {
        User user =
                userRepository.findById(userId).orElseThrow(() -> new UserException(USER_NOT_FOUND));

        chatbotRepository.deleteAllByUserId(user.getId());
    }

    @Transactional
    public String getTips(final Long userId) {
        User user =
                userRepository.findById(userId).orElseThrow(() -> new UserException(USER_NOT_FOUND));

        // 챗봇의 응답 저장
        chatbotRepository.save(
                ChatMessage.builder().user(user).message(TIPS).sender(Sender.CHATBOT).build());

        return TIPS;
    }

    @Scheduled(cron = "0 0 4 * * ?") // 매일 4시에 실행
    @Transactional
    public void scheduledMessageCleanUp() {
        deleteOldChatMessages();
        log.info("✅ Chat messages older than 30 days have been deleted.");
    }

    private void deleteOldChatMessages() {
        chatbotRepository.deleteAllByCreatedDateBefore(LocalDateTime.now().minusDays(30));
    }
}
