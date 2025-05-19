package com.ripple.BE.post.dto.response;

import com.ripple.BE.post.domain.comment.Comment;
import com.ripple.BE.post.domain.type.PostType;

public record LikeCommentResponseDTO(
        Long id, String content, String postName, PostType type, String createdDate) {

    public static LikeCommentResponseDTO from(Comment comment) {
        return new LikeCommentResponseDTO(
                comment.getId(),
                comment.getContent(),
                comment.getPost().getTitle(),
                comment.getPost().getType(),
                comment.getCreatedDate().toString());
    }
}
