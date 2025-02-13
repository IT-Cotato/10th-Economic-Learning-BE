package com.ripple.BE.user.dto;

import com.ripple.BE.user.domain.User;

public record CommunityUserDTO(Long id, String nickname, String profileImageUrl) {

    public static CommunityUserDTO toCommunityUserDTO(final User user) {
        if (user == null) {
            return new CommunityUserDTO(null, null, null);
        }

        return new CommunityUserDTO(
                user.getId(),
                user.getNickname(),
                user.getProfileImage() == null ? null : user.getProfileImage().getS3Info().getUrl());
    }
}
