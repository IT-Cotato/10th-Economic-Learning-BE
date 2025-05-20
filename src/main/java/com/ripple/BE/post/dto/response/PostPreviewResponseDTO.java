package com.ripple.BE.post.dto.response;

import com.ripple.BE.global.utils.RelativeTimeFormatter;
import com.ripple.BE.post.domain.post.Post;
import com.ripple.BE.post.domain.type.PostType;

public record PostPreviewResponseDTO(
        Long id,
        String title,
        String content,
        PostType type,
        long likeCount,
        long commentCount,
        String imageUrl,
        boolean isScraped,
        String createdDate) {

    public static PostPreviewResponseDTO of(Post post, String imageUrl, boolean isScraped) {
        return new PostPreviewResponseDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getType(),
                post.getLikeCount(),
                post.getCommentCount(),
                imageUrl,
                isScraped,
                RelativeTimeFormatter.formatRelativeTime(post.getCreatedDate()));
    }
}
