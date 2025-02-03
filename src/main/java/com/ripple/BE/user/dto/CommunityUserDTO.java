package com.ripple.BE.user.dto;

import com.ripple.BE.image.domain.Image;
import com.ripple.BE.user.domain.User;

public record CommunityUserDTO(Long id, String nickname, Image profileImage) {

    public static CommunityUserDTO toCommunityUserDTO(final User user) {
        return new CommunityUserDTO(user.getId(), user.getNickname(), user.getProfileImage());
    }
}
