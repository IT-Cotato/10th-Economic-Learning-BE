package com.ripple.BE.auth.dto;

public record KakaoUserInfoResponse(
        Long id, String connected_at, KakaoProperties properties, KakaoAccount kakao_account) {}
