package com.ripple.BE.auth.dto;

public record KakaoUserInfoResponse(
        Long id, KakaoProperties properties, KakaoAccount kakao_account) {}
