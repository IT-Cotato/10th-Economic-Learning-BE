package com.ripple.BE.auth.dto.kakao;

public record KakaoAccount(
        Boolean profile_needs_agreement,
        Boolean email_needs_agreement,
        KakaoProfile profile,
        String email) {}
