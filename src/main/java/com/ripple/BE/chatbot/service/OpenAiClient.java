package com.ripple.BE.chatbot.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.ripple.BE.chatbot.dto.response.OpenAiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OpenAiClient {

	@Value("${spring.ai.openai.api-key}")
	private String apiKey;

	private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

	private static final String PROMPT = """
			당신은 경제학습 서비스를 위한 AI 챗봇입니다.
			
			## 역할 및 답변 원칙:
			1. 사용자의 질문이 경제와 **조금이라도 관련**되어 있다면, 풍부한 정보와 예시를 포함하여 상세하게 답변하세요.
			2. 경제 개념뿐만 아니라 **소비, 생산, 투자, 시장, 금융, 기업, 정책, 가격 변동, 경제 이슈 등** 다양한 질문에 답변할 수 있습니다.
			3. 경제와 **완전히 무관한 질문**에는 답변하지 않으며, 다음과 같이 응답합니다:
			   "죄송합니다. 저는 경제 관련 질문에만 답변할 수 있습니다."
			4. **모든 답변은 반드시 한국어로 제공**해야 합니다.
			
			## 답변 스타일:
			- 기본적인 개념을 먼저 설명한 후, **추가적인 배경 정보와 실생활 예시**를 제공합니다.
			- 핵심 개념을 강조하기 위해 **중요한 단어에 대해 짧은 정의를 포함**하세요.
			- 질문이 너무 짧다면, 질문의 의도를 보완하여 더 풍부한 정보를 제공하세요.
			- 개념을 쉽게 이해할 수 있도록 **추가 설명, 예시, 배경 지식, 실생활 적용 사례**를 포함하세요.
	
			""";

	public String getResponseFromOpenAi(String question) {
		WebClient webClient = WebClient.builder()
			.baseUrl(OPENAI_API_URL)
			.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.build();

		Map<String, Object> requestBody = Map.of(
			"model", "gpt-3.5-turbo",
			"messages", List.of(
				Map.of("role", "system", "content", PROMPT),
				Map.of("role", "user", "content", question)
			)
		);

		try {
			return webClient.post()
				.bodyValue(requestBody)
				.retrieve()
				.bodyToMono(OpenAiResponse.class)
				.map(res -> res.getChoices().get(0).getMessage().getContent())
				.block();
		} catch (WebClientResponseException e) {
			log.error("❌ OpenAI API 응답 에러: {}", e.getResponseBodyAsString());
			throw new RuntimeException("OpenAI API 호출 실패: " + e.getMessage());
		} catch (Exception e) {
			log.error("❌ OpenAI API 예외 발생: {}", e.getMessage());
			throw new RuntimeException("OpenAI API 호출 중 예외 발생");
		}
	}
}
