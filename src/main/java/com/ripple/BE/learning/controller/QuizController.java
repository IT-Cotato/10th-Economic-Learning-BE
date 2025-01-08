package com.ripple.BE.learning.controller;

import com.ripple.BE.global.dto.response.ApiResponse;
import com.ripple.BE.learning.dto.QuizListDTO;
import com.ripple.BE.learning.dto.QuizResultDTO;
import com.ripple.BE.learning.dto.request.SubmitAnswerRequest;
import com.ripple.BE.learning.dto.response.QuizListResponse;
import com.ripple.BE.learning.dto.response.QuizResultResponse;
import com.ripple.BE.learning.service.quiz.QuizService;
import com.ripple.BE.user.domain.CustomUserDetails;
import com.ripple.BE.user.domain.type.Level;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/learning")
@Tag(name = "Learning", description = "학습 API")
public class QuizController {

    private final QuizService quizService;

    @Operation(summary = "퀴즈 시작", description = "퀴즈를 시작합니다.")
    @PostMapping("/{learningSetId}/quizzes")
    public ResponseEntity<ApiResponse<Object>> startQuiz(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("learningSetId") long learningSetId,
            final @RequestParam(defaultValue = "BEGINNER") Level level) {

        QuizListDTO quizListDTO = quizService.startQuiz(currentUser.getId(), learningSetId, level);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(QuizListResponse.toQuizListResponse(quizListDTO)));
    }

    @Operation(summary = "퀴즈 제출", description = "퀴즈를 제출 후 정답 여부와 해설을 반환합니다.")
    @PostMapping("/{learningSetId}/quizzes/{quizId}")
    public ResponseEntity<ApiResponse<Object>> submitAnswer(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("learningSetId") long learningSetId,
            final @PathVariable("quizId") long quizId,
            final @RequestBody @Valid SubmitAnswerRequest request) {

        QuizResultDTO quizResultDTO =
                quizService.submitAnswer(currentUser.getId(), quizId, request.answerIndex());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(QuizResultResponse.toQuizResultResponse(quizResultDTO)));
    }

    @Operation(summary = "퀴즈 완료", description = "퀴즈를 완료합니다.")
    @PostMapping("/{learningSetId}/quizzes/end")
    public ResponseEntity<ApiResponse<Object>> finishQuiz(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("learningSetId") long learningSetId,
            final @RequestParam(defaultValue = "BEGINNER") Level level) {

        quizService.finishQuiz(currentUser.getId(), learningSetId, level);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }
}
