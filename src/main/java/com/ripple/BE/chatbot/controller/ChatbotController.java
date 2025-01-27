package com.ripple.BE.chatbot.controller;

import com.ripple.BE.chatbot.dto.ChatDTO;
import com.ripple.BE.chatbot.dto.request.ChatRequest;
import com.ripple.BE.chatbot.dto.response.ChatResponse;
import com.ripple.BE.chatbot.service.ChatbotService;
import com.ripple.BE.global.dto.response.ApiResponse;
import com.ripple.BE.user.domain.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/chatbot")
@Tag(name = "Chatbot", description = "챗봇 API")
public class ChatbotController {

    private final ChatbotService chatbotService;

    @Operation(summary = "챗봇에게 메세지 보내기", description = "챗봇에게 메세지를 보내고 응답을 받습니다. 대화 내용을 저장합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<Object>> sendMessage(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @Valid ChatRequest request) {

        ChatResponse chatResponse =
                chatbotService.sendMessage(ChatDTO.tochatDTO(request), currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(chatResponse));
    }

    @Operation(summary = "대화 내역 조회", description = "챗봇과의 대화 내역을 조회합니다.")
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<Object>> getMessages(
            final @AuthenticationPrincipal CustomUserDetails currentUser) {

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }

    @Operation(summary = "대화 내역 초기화", description = "챗봇과의 대화 내역을 초기화합니다.")
    @PostMapping("/clear")
    public ResponseEntity<ApiResponse<Object>> clearMessages(
            final @AuthenticationPrincipal CustomUserDetails currentUser) {

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }
}
