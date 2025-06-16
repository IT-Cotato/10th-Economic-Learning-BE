package com.ripple.BE.user.dto.response;

import com.ripple.BE.global.utils.RelativeTimeFormatter;
import com.ripple.BE.post.persistence.dto.CommentWithPostDTO;

public record UserCommentResponseDTO(
        Long commentId, String comment, String postTitle, String type, String createdDate) {
    public static UserCommentResponseDTO from(CommentWithPostDTO commentWithPostDTO) {
        return new UserCommentResponseDTO(
                commentWithPostDTO.commentId(),
                commentWithPostDTO.commentContent(),
                commentWithPostDTO.postTitle(),
                commentWithPostDTO.postType().name(),
                RelativeTimeFormatter.formatRelativeTime(commentWithPostDTO.commentCreatedDate()));
    }
}
