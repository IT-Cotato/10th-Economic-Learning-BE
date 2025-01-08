package com.ripple.BE.user.controller;

import com.ripple.BE.global.dto.response.ApiResponse;
import com.ripple.BE.user.domain.CustomUserDetails;
import com.ripple.BE.user.dto.ProgressDTO;
import com.ripple.BE.user.dto.ProgressResponse;
import com.ripple.BE.user.dto.UpdateUserProfileRequest;
import com.ripple.BE.user.service.UserProgressService;
import com.ripple.BE.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "User", description = "유저 API")
public class UserController {

    private final UserService userService;
    private final UserProgressService userProgressService;

    @Operation(summary = "프로필 등록", description = "로그인 후 유저의 프로필을 등록합니다.")
    @PostMapping("/profile")
    public ResponseEntity<ApiResponse<?>> profile(
            @Valid @RequestBody UpdateUserProfileRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        userService.updateProfile(request, customUserDetails.getId());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.EMPTY_RESPONSE);
    }

    @Operation(
            summary = "레벨별 학습 진도율 조회",
            description = "레베벨 진도율을 조회합니다. 100퍼센트 중 몇 퍼센트를 완료했는지 반환합니다.")
    @GetMapping("/progress")
    public ResponseEntity<ApiResponse<Object>> getUserLearningProgress(
            final @AuthenticationPrincipal CustomUserDetails currentUser) {

        ProgressDTO progressDTO = userProgressService.getLearningSetCompletionRate(currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(ProgressResponse.toProgressResponse(progressDTO)));
    }
}
