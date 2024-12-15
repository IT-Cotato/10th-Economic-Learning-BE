package com.ripple.BE.auth.controller;

import com.ripple.BE.auth.dto.LoginRequest;
import com.ripple.BE.auth.dto.SignupRequest;
import com.ripple.BE.auth.service.AuthService;
import com.ripple.BE.global.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "인증 API")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "카카오 로그인", description = "카카오 로그인을 진행합니다.")
    @GetMapping("/login/kakao")
    public ResponseEntity<ApiResponse<Object>> kakaoLogin(@RequestParam String code) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(authService.kakaoLogin(code)));
    }

    @Operation(summary = "기본 회원 가입", description = "이메일과 비밀번호로 회원가입합니다.")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<?>> signUp(@Valid @RequestBody SignupRequest request) {
        authService.BasicSignup(request);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.EMPTY_RESPONSE);
    }

    @Operation(summary = "기본 로그인", description = "이메일과 비밀번호로 로그인합니다.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@Valid @RequestBody LoginRequest request) {
        String token = authService.BasicLogin(request);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(token));
    }
}
