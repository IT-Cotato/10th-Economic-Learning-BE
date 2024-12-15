package com.ripple.BE.auth.dto.kakao;

public record KakaoUserInfoResponse(
        Long id, KakaoProperties properties, KakaoAccount kakao_account) {}
