package com.ripple.BE.chatbot.service;

import static com.ripple.BE.user.exception.errorcode.UserErrorCode.*;

import java.time.LocalDateTime;

import com.ripple.BE.chatbot.domain.ChatMessage;
import com.ripple.BE.chatbot.domain.type.Sender;
import com.ripple.BE.chatbot.dto.ChatDTO;
import com.ripple.BE.chatbot.dto.ChatListDTO;
import com.ripple.BE.chatbot.dto.response.ChatResponse;
import com.ripple.BE.chatbot.repository.ChatbotRepository;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.exception.UserException;
import com.ripple.BE.user.repository.UserRepository;

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

	private static final int PAGE_SIZE = 10;

	private static final String TIPS = """
		리플의 AI 챗봇을 200% 활용하는 프롬프트 꿀팁
		  
		1. 명확하고 구체적으로 작성하기
		    - 무엇을 원하는지 구체적으로 설명해 보세요!
		    - 예시 : "복리를 이해하기 위해, 연 5% 이자율로 3년 동안 100만 원이 어떻게 증가하는지 구체적으로 계산해줘."
		2. 배경 정보 제공하기
		    - 질문이나 질문자의 배경 정보를 알려주세요!
		    - 예시 : "경제를 공부하는 대학생인데, 단리와 복리의 차이를 쉽게 이해할 수 있도록 설명해줘."
		3. 결과물 형식 명시하기
		    - 결과물을 어떤 형태로 제공받고 싶은지 알려주세요!
		    - 예시 : “단리와 복리의 차이를 표로 정리하고, 간단한 계산 예를 포함해 설명해줘.\"""";

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
		ChatMessage saved = chatbotRepository.save(
			ChatMessage.builder()
				.user(user)
				.message(response)
				.sender(Sender.CHATBOT)
				.build());

		return ChatResponse.toChatResponse(saved);
    }

	public ChatListDTO getChatList(final Long userId, final int page) {
		Pageable pageable = PageRequest.of(page, PAGE_SIZE);

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserException(USER_NOT_FOUND));

		Page<ChatMessage> chatMessagePage = chatbotRepository.findAllByUserIdOrderByCreatedDateDesc(
			user.getId(), pageable);

		return ChatListDTO.toChatListDTO(chatMessagePage);
	}

	@Transactional
	public void clearChat(final Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserException(USER_NOT_FOUND));

		chatbotRepository.deleteAllByUserId(user.getId());
	}

	@Transactional
	public String getTips(final Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserException(USER_NOT_FOUND));

		// 챗봇의 응답 저장
		chatbotRepository.save(
			ChatMessage.builder()
				.user(user)
				.message(TIPS)
				.sender(Sender.CHATBOT)
				.build());

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
