package com.ripple.BE.post.dto.response;

import com.ripple.BE.global.utils.RelativeTimeFormatter;
import com.ripple.BE.post.domain.type.PostType;
import com.ripple.BE.post.persistence.dto.PostWithScrapAndImageDTO;

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

    public static PostPreviewResponseDTO from(PostWithScrapAndImageDTO postWithScrapAndImageDTO) {
        return new PostPreviewResponseDTO(
                postWithScrapAndImageDTO.id(),
                postWithScrapAndImageDTO.title(),
                postWithScrapAndImageDTO.content(),
                postWithScrapAndImageDTO.type(),
                postWithScrapAndImageDTO.likeCount(),
                postWithScrapAndImageDTO.commentCount(),
                postWithScrapAndImageDTO.imageUrl(),
                postWithScrapAndImageDTO.isScraped(),
                RelativeTimeFormatter.formatRelativeTime(postWithScrapAndImageDTO.createdDate()));
    }
}
