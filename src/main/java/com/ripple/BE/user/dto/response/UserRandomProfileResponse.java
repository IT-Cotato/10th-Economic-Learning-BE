package com.ripple.BE.user.dto.response;

import com.ripple.BE.user.dto.UserRandomProfileDTO;

public record UserRandomProfileResponse(String nickname, String profileImageUrl) {
    public static UserRandomProfileResponse toUserRandomProfileResponse(
            UserRandomProfileDTO userRandomProfileDTO) {
        return new UserRandomProfileResponse(
                userRandomProfileDTO.nickname(), userRandomProfileDTO.profileImageUrl());
    }
}
