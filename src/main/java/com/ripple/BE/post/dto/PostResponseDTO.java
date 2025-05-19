package com.ripple.BE.post.dto;

import com.ripple.BE.global.utils.RelativeTimeFormatter;
import com.ripple.BE.image.dto.response.ImageResponse;
import com.ripple.BE.post.domain.post.Post;
import com.ripple.BE.post.domain.type.PostType;
import java.util.List;

public record PostResponseDTO(
        String title,
        String author,
        Long authorId,
        String authorProfileImage,
        String content,
        PostType type,
        long likeCount,
        long commentCount,
        long scrapCount,
        boolean isScraped,
        boolean isLiked,
        boolean isAuthor,
        List<ImageResponse> imageList,
        String createdDate,
        List<CommentResponseDTO> commentList) {

    public static PostResponseDTO of(
            Post post,
            List<ImageResponse> imageList,
            List<CommentResponseDTO> commentList,
            boolean isScraped,
            boolean isLiked,
            boolean isAuthor) {
        return new PostResponseDTO(
                post.getTitle(),
                post.getAuthor().getNickname(),
                post.getAuthor().getId(),
                post.getAuthor().getProfileImage().toString(),
                post.getContent(),
                post.getType(),
                post.getLikeCount(),
                post.getCommentCount(),
                post.getScrapCount(),
                isScraped,
                isLiked,
                isAuthor,
                imageList,
                RelativeTimeFormatter.formatRelativeTime(post.getCreatedDate()),
                commentList);
    }
}
