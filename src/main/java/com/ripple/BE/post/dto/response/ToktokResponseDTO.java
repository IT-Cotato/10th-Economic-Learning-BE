package com.ripple.BE.post.dto.response;

import com.ripple.BE.global.utils.RelativeTimeFormatter;
import com.ripple.BE.image.dto.response.ImageResponse;
import com.ripple.BE.post.domain.post.Post;
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
            Post toktokPost,
            List<ImageResponse> imageList,
            List<CommentResponseDTO> commentList,
            boolean isScraped,
            boolean isLiked) {
        return new ToktokResponseDTO(
                toktokPost.getTitle(),
                toktokPost.getContent(),
                toktokPost.getCommentCount(),
                toktokPost.getLikeCount(),
                toktokPost.getScrapCount(),
                isScraped,
                isLiked,
                imageList,
                commentList,
                RelativeTimeFormatter.formatRelativeTime(toktokPost.getCreatedDate()));
    }
}
