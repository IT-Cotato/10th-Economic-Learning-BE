package com.ripple.BE.auth.controller;

import com.ripple.BE.auth.service.AuthService;
import com.ripple.BE.global.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/auth")
@Tag(name = "Auth V2", description = "인증 API (v2)")
public class AuthControllerV2 {

    private final AuthService authService;

    @Operation(summary = "카카오 로그인 (v2)", description = "네이티브 앱에서 카카오 로그인을 진행합니다.")
    @PostMapping("/login/kakao")
    public ResponseEntity<ApiResponse<Object>> kakaoLogin(@RequestParam String accessToken) {

        String jwtToken = authService.kakaoLoginV2(accessToken);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(jwtToken));
    }
}
