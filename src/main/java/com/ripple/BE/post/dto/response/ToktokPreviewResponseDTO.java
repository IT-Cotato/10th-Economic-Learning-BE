package com.ripple.BE.post.dto.response;

import com.ripple.BE.global.utils.RelativeTimeFormatter;
import com.ripple.BE.post.persistence.dto.ToktokWithImageDTO;
import com.ripple.BE.user.domain.User;
import java.util.List;

public record ToktokPreviewResponseDTO(
        Long id,
        String title,
        long participantCount,
        long commentCount,
        long likeCount,
        long scrapCount,
        String imageUrl,
        String createdDate,
        List<UserRandomProfileDTO> userProfiles // 중첩 DTO
        ) {
    public record UserRandomProfileDTO(String nickname, String profileImageUrl) {
        public static UserRandomProfileDTO from(User user) {
            return new UserRandomProfileDTO(
                    user.getNickname(),
                    user.getProfileImage() == null ? null : user.getProfileImage().getS3Info().getUrl());
        }
    }

    public static ToktokPreviewResponseDTO of(ToktokWithImageDTO dto, List<User> users) {
        return new ToktokPreviewResponseDTO(
                dto.id(),
                dto.title(),
                dto.participantCount(),
                dto.commentCount(),
                dto.likeCount(),
                dto.scrapCount(),
                dto.imageUrl(),
                RelativeTimeFormatter.formatRelativeTime(dto.usedDate().atStartOfDay()),
                users.stream().map(UserRandomProfileDTO::from).toList());
    }
}
