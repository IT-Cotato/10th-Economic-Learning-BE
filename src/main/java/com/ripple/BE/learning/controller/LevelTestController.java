package com.ripple.BE.learning.controller;

import com.ripple.BE.global.dto.response.ApiResponse;
import com.ripple.BE.learning.application.quiz.LevelTestService;
import com.ripple.BE.learning.application.quiz.command.SubmitLevelTestCommand;
import com.ripple.BE.learning.dto.request.SubmitLevelTestRequest;
import com.ripple.BE.learning.dto.response.leveltest.LevelTestQuizStartResponseDTO;
import com.ripple.BE.learning.dto.response.leveltest.LevelTestResultResponseDTO;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/level-test")
@Tag(name = "LevelTest", description = "레벨 테스트 API")
public class LevelTestController {

    private final LevelTestService levelTestService;

    @GetMapping("/quiz")
    @Operation(
            summary = "레벨 테스트 퀴즈 목록 조회",
            description =
                    "레벨 테스트 퀴즈 목록 조회를 위한 API 입니다. 인증 없이 접근가능합니다. 각 레벨에서 랜덤으로 3개씩 가져옵니다. 프론트에서 문제 id와 유저 입력한 정답 캐싱이 필요합니다.")
    public ResponseEntity<ApiResponse<?>> getLevelTestQuizList() {

        LevelTestQuizStartResponseDTO levelTestQuizStartResponseDTO =
                levelTestService.getLevelTestQuizList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(levelTestQuizStartResponseDTO));
    }

    @PostMapping("/result")
    @Operation(
            summary = "레벨 테스트 결과 제출",
            description =
                    "레벨 테스트 결과 제출을 위한 API 입니다. 테스트 종료 후, 각 문제 id와 답안을 한번에 서버로 전송하면 정답률, 유저의 레벨, 틀린문제에 대한 해설을 반환합니다."
                            + "레벨 테스트 시작 시 전달받은 levelTestSessionKey를 함께 전달해야 합니다. "
                            + "레벨 테스트 세션 키는 프론트에서 관리하며, 레벨 테스트 시작 시 서버로부터 전달받습니다.")
    public ResponseEntity<ApiResponse<?>> submitLevelTestResult(
            @Valid @RequestBody SubmitLevelTestRequest request,
            @RequestParam(required = true) String levelTestSessionKey,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        SubmitLevelTestCommand submitLevelTestCommand = SubmitLevelTestCommand.from(request);

        LevelTestResultResponseDTO levelTestResultResponseDTO =
                levelTestService.submitLevelTestResult(
                        submitLevelTestCommand, levelTestSessionKey, customUserDetails.getId());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(levelTestResultResponseDTO));
    }
}
