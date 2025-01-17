package com.ripple.BE.learning.controller;

import com.ripple.BE.global.dto.response.ApiResponse;
import com.ripple.BE.learning.dto.QuizDTO;
import com.ripple.BE.learning.dto.QuizListDTO;
import com.ripple.BE.learning.dto.QuizSubmitDTO;
import com.ripple.BE.learning.dto.request.AddLevelTestQuizRequest;
import com.ripple.BE.learning.dto.request.SubmitLevelTestRequest;
import com.ripple.BE.learning.dto.response.LevelTestQuizListResponse;
import com.ripple.BE.learning.dto.response.LevelTestResultResponse;
import com.ripple.BE.learning.service.LevelTestService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/level-test")
@Tag(name = "LevelTest", description = "레벨 테스트 API")
public class LevelTestController {

    private final LevelTestService levelTestService;

    @PostMapping
    @Operation(summary = "레벨 테스트 퀴즈 추가", description = "레벨 테스트 더미 데이터 추가를 위한 API 입니다.")
    public ResponseEntity<ApiResponse<?>> addLevelTestQuiz(
            @Valid @RequestBody AddLevelTestQuizRequest request) {

        levelTestService.addLevelTestQuiz(QuizDTO.toQuizDTO(request));

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.EMPTY_RESPONSE);
    }

    @GetMapping("/quiz")
    @Operation(
            summary = "레벨 테스트 퀴즈 목록 조회",
            description = "레벨 테스트 퀴즈 목록 조회를 위한 API 입니다. 인증 없이 접근가능합니다.")
    public ResponseEntity<ApiResponse<?>> getLevelTestQuizList() {
        QuizListDTO levelTestQuizList = levelTestService.getLevelTestQuizList();

        LevelTestQuizListResponse levelTestQuizListResponse =
                LevelTestQuizListResponse.toLevelTestQuizListResponse(levelTestQuizList);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(levelTestQuizListResponse));
    }

    @PostMapping("/result")
    @Operation(summary = "레벨 테스트 결과 제출", description = "레벨 테스트 결과 제출을 위한 API 입니다.")
    public ResponseEntity<ApiResponse<?>> submitLevelTestResult(
            @Valid @RequestBody SubmitLevelTestRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        LevelTestResultResponse levelTestResultResponse =
                levelTestService.submitLevelTestResult(
                        QuizSubmitDTO.toQuizDto(request), customUserDetails.getId());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(levelTestResultResponse));
    }
}
