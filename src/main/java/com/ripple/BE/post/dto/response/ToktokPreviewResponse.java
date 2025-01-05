package com.ripple.BE.post.dto.response;

import com.ripple.BE.global.utils.RelativeTimeFormatter;
import com.ripple.BE.post.dto.PostDTO;

public record ToktokPreviewResponse(
        Long id, String title, long participantCount, String imageUrl, String createdDate) {

    public static ToktokPreviewResponse toToktokPreviewResponse(PostDTO postDTO) {

        return new ToktokPreviewResponse(
                postDTO.id(),
                postDTO.title(),
                postDTO.commentCount(),
                postDTO.imageList().imageDTOList().isEmpty()
                        ? null
                        : postDTO.imageList().imageDTOList().get(0).url(),
                RelativeTimeFormatter.formatRelativeTime(postDTO.usedDate().atStartOfDay()));
    }
}
