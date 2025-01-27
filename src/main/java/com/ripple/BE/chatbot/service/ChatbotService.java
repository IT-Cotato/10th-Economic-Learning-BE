package com.ripple.BE.chatbot.service;

import static com.ripple.BE.user.exception.errorcode.UserErrorCode.*;

import com.ripple.BE.chatbot.domain.ChatMessage;
import com.ripple.BE.chatbot.domain.type.Sender;
import com.ripple.BE.chatbot.dto.ChatDTO;
import com.ripple.BE.chatbot.dto.ChatListDTO;
import com.ripple.BE.chatbot.dto.response.ChatListResponse;
import com.ripple.BE.chatbot.dto.response.ChatResponse;
import com.ripple.BE.chatbot.repository.ChatbotRepository;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.exception.UserException;
import com.ripple.BE.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        // 프롬프트 포함하여 OpenAI API 호출
        String prompt = """
			당신은 경제학습 서비스를 위한 AI 챗봇입니다.
			오직 경제와 관련된 질문에만 답변해야 하며, 경제와 무관한 질문에는 답변하지 않습니다.
			모든 답변은 반드시 한국어로 제공해야 합니다.
			경제 이외의 주제에 대한 질문에는 다음과 같이 답변하세요: 
			"죄송합니다. 저는 경제 관련 질문에만 답변할 수 있습니다."
			""";

        // OpenAI API 호출
        String response = openAiChatClient.call(prompt + "\n사용자 질문: " + chatDTO.message());

        // 유저의 메세지 저장
        chatbotRepository.save(
            ChatMessage.builder()
                .user(user)
                .message(chatDTO.message())
                .sender(Sender.USER)
                .build());

        // 챗봇의 응답 저장
        chatbotRepository.save(
            ChatMessage.builder()
                .user(user)
                .message(response)
                .sender(Sender.CHATBOT)
                .build());

        return new ChatResponse(response);
    }

	public ChatListDTO getChatList(final Long userId, final Pageable pageable) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserException(USER_NOT_FOUND));

		Page<ChatMessage> page = chatbotRepository.findAllByUserIdOrderByCreatedDate(
			user.getId(), pageable);

		return ChatListDTO.builder()
			.chatDTOList(page.getContent().stream().map(ChatDTO::toChatDTO).toList())
			.currentPage(page.getNumber())
			.totalPage(page.getTotalPages())
			.build();
	}
}
