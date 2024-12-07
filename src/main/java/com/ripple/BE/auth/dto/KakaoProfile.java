package com.ripple.BE.auth.dto;

public record KakaoProfile(
        String nickname,
        String thumbnail_image, // 프로필 미리보기 이미지 URL
        String profile_image_url, // 프로필 이미지 URL
        Boolean is_default_image) {}
