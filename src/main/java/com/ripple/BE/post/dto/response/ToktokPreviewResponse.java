package com.ripple.BE.post.dto.response;

import com.ripple.BE.global.utils.RelativeTimeFormatter;
import com.ripple.BE.post.dto.PostDTO;

public record ToktokPreviewResponse(
        Long id,
        String title,
        long participantCount,
        long likeCount,
        String imageUrl,
        Boolean isScraped,
        String createdDate) {

    public static ToktokPreviewResponse toToktokPreviewResponse(PostDTO postDTO) {

        return new ToktokPreviewResponse(
                postDTO.id(),
                postDTO.title(),
                postDTO.commentCount(),
                postDTO.likeCount(),
                postDTO.imageList().imageDTOList().isEmpty()
                        ? null
                        : postDTO.imageList().imageDTOList().get(0).url(),
                postDTO.isScraped(),
                RelativeTimeFormatter.formatRelativeTime(postDTO.usedDate().atStartOfDay()));
    }
}
