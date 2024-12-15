package com.ripple.BE.auth.dto;

public record SignupRequest(
        // @Email(message = "Email 형식이 아닙니다.")
        String accountEmail,
        // @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
        String password) {}
