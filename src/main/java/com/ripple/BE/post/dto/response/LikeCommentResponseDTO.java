package com.ripple.BE.post.dto.response;

import com.ripple.BE.global.utils.RelativeTimeFormatter;
import com.ripple.BE.post.domain.type.PostType;
import com.ripple.BE.post.persistence.dto.LikeCommentWithPostDTO;

public record LikeCommentResponseDTO(
        Long id, String content, String postName, PostType type, String createdDate) {

    public static LikeCommentResponseDTO from(LikeCommentWithPostDTO dto) {
        return new LikeCommentResponseDTO(
                dto.commentId(),
                dto.commentContent(),
                dto.postTitle(),
                dto.postType(),
                RelativeTimeFormatter.formatRelativeTime(dto.createdDate()));
    }
}
