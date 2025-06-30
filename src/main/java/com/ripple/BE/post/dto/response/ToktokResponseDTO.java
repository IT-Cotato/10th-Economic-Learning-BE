package com.ripple.BE.post.dto.response;

import com.ripple.BE.global.utils.RelativeTimeFormatter;
import com.ripple.BE.image.dto.response.ImageResponse;
import com.ripple.BE.post.persistence.dto.ToktokDetailDTO;
import java.util.List;

public record ToktokResponseDTO(
        String title,
        String content,
        long participantCount,
        long likeCount,
        long scrapCount,
        boolean isScraped,
        boolean isLiked,
        List<ImageResponse> imageList,
        List<CommentResponseDTO> commentList,
        String createdDate) {

    public static ToktokResponseDTO of(
            ToktokDetailDTO dto,
            List<ImageResponse> imageList,
            List<CommentResponseDTO> commentList,
            long participantCount) {
        return new ToktokResponseDTO(
                dto.title(),
                dto.content(),
                participantCount,
                dto.likeCount(),
                dto.scrapCount(),
                dto.isScraped(),
                dto.isLiked(),
                imageList,
                commentList,
                RelativeTimeFormatter.formatRelativeTime(dto.usedDate().atStartOfDay()));
    }
}
