package com.ripple.BE.user.dto;

import com.ripple.BE.image.dto.ImageDTO;
import com.ripple.BE.user.domain.User;

public record CommunityUserDTO(Long id, String nickname, ImageDTO profileImage) {

    public static CommunityUserDTO toCommunityUserDTO(final User user) {
        return new CommunityUserDTO(
                user.getId(), user.getNickname(), ImageDTO.toImageDTO(user.getProfileImageUrl()));
    }
}
