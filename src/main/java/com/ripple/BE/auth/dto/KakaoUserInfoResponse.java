package com.ripple.BE.auth.dto;

import lombok.Getter;

@Getter
public record KakaoUserInfoResponse(
        Long id, String connected_at, KakaoProperties properties, KakaoAccount kakao_account) {}
