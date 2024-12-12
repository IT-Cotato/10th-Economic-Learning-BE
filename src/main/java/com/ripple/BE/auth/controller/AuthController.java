package com.ripple.BE.auth.controller;

import com.ripple.BE.auth.service.KakaoAuthService;
import com.ripple.BE.global.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "인증 API")
public class AuthController {

    private final KakaoAuthService kakaoAuthService;

    @Operation(summary = "카카오 로그인", description = "카카오 로그인을 진행합니다.")
    @GetMapping("/login/kakao")
    public ResponseEntity<ApiResponse<Object>> kakaoLogin(@RequestParam String code) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(kakaoAuthService.kakaoLogin(code)));
    }
}
