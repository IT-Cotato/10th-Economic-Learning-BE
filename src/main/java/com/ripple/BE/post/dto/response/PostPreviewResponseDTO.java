package com.ripple.BE.post.dto.response;

import com.ripple.BE.global.utils.RelativeTimeFormatter;
import com.ripple.BE.post.domain.type.PostType;
import com.ripple.BE.post.persistence.dto.PostWithImageDTO;

public record PostPreviewResponseDTO(
        Long id,
        String title,
        String content,
        PostType type,
        long likeCount,
        long commentCount,
        String imageUrl,
        String createdDate) {

    public static PostPreviewResponseDTO from(PostWithImageDTO postWithImageDTO) {
        return new PostPreviewResponseDTO(
                postWithImageDTO.id(),
                postWithImageDTO.title(),
                postWithImageDTO.content(),
                postWithImageDTO.type(),
                postWithImageDTO.likeCount(),
                postWithImageDTO.commentCount(),
                postWithImageDTO.imageUrl(),
                RelativeTimeFormatter.formatRelativeTime(postWithImageDTO.createdDate()));
    }
}
