package com.ripple.BE.learning.controller;

import com.ripple.BE.global.dto.response.ApiResponse;
import com.ripple.BE.learning.dto.QuizListDTO;
import com.ripple.BE.learning.dto.QuizResultDTO;
import com.ripple.BE.learning.dto.request.QuizListRequest;
import com.ripple.BE.learning.dto.request.SubmitAnswerRequest;
import com.ripple.BE.learning.dto.response.QuizListResponse;
import com.ripple.BE.learning.dto.response.QuizResultResponse;
import com.ripple.BE.learning.service.QuizService;
import com.ripple.BE.user.domain.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/learning")
@Tag(name = "Learning", description = "학습 API")
public class QuizController {

    private final QuizService quizService;

    @Operation(summary = "퀴즈 시작", description = "퀴즈를 시작합니다.")
    @PostMapping("/quiz")
    public ResponseEntity<ApiResponse<Object>> startQuiz(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @RequestBody @Valid QuizListRequest request) {

        QuizListDTO quizListDTO =
                quizService.startQuiz(currentUser.getId(), request.learningSetName(), request.level());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(QuizListResponse.toQuizListResponse(quizListDTO)));
    }

    @Operation(summary = "퀴즈 제출", description = "퀴즈를 제출 후 정답 여부와 해설을 반환합니다.")
    @PostMapping("/quiz/submit")
    public ResponseEntity<ApiResponse<Object>> submitAnswer(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @RequestBody @Valid SubmitAnswerRequest request) {

        QuizResultDTO quizResultDTO =
                quizService.submitAnswer(currentUser.getId(), request.quizId(), request.answerIndex());
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(QuizResultResponse.toQuizResultResponse(quizResultDTO)));
    }

    @Operation(summary = "퀴즈 완료", description = "퀴즈를 완료하고 결과를 반환합니다.")
    @PostMapping("/quiz/end")
    public ResponseEntity<ApiResponse<Object>> finishQuiz(
            final @AuthenticationPrincipal CustomUserDetails currentUser) {

        QuizResultDTO quizResultDTO = quizService.finishQuiz(currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(QuizResultResponse.toQuizResultResponse(quizResultDTO)));
    }
}
