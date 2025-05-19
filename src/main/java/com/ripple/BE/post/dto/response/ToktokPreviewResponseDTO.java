package com.ripple.BE.post.dto.response;

import com.ripple.BE.global.utils.RelativeTimeFormatter;
import com.ripple.BE.post.domain.post.Post;
import com.ripple.BE.user.dto.UserRandomProfileListDTO;
import com.ripple.BE.user.dto.response.UserRandomProfileListResponse;

public record ToktokPreviewResponseDTO(
        Long id,
        String title,
        long participantCount,
        long likeCount,
        long scrapCount,
        String imageUrl,
        Boolean isScraped,
        String createdDate,
        UserRandomProfileListResponse userRandomProfileListResponse) {

    public static ToktokPreviewResponseDTO of(
            Post toktokPost,
            String imageUrl,
            boolean isScraped,
            UserRandomProfileListDTO userRandomProfileListDTO) {
        return new ToktokPreviewResponseDTO(
                toktokPost.getId(),
                toktokPost.getTitle(),
                toktokPost.getCommentCount(),
                toktokPost.getLikeCount(),
                toktokPost.getScrapCount(),
                imageUrl,
                isScraped,
                RelativeTimeFormatter.formatRelativeTime(toktokPost.getCreatedDate()),
                userRandomProfileListDTO == null
                        ? null
                        : UserRandomProfileListResponse.toUserRandomProfileListResponse(
                                userRandomProfileListDTO));
    }
}
