package com.ripple.BE.user.dto;

import com.ripple.BE.user.domain.User;

public record UserRandomProfileDTO(String nickname, String profileImageUrl) {
    public static UserRandomProfileDTO toUserRandomProfileDTO(User user) {
        return new UserRandomProfileDTO(
                user.getNickname(),
                user.getProfileImage() == null ? null : user.getProfileImage().getS3Info().getUrl());
    }
}
