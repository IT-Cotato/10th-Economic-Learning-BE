package com.ripple.BE.post.dto;

import com.ripple.BE.post.domain.Comment;
import com.ripple.BE.post.domain.type.PostType;
import java.time.LocalDateTime;

public record LikeCommentDTO(
        Long id, String content, String postName, PostType type, LocalDateTime createdDate) {

    public static LikeCommentDTO toLikeCommentDTO(Comment comment) {
        return new LikeCommentDTO(
                comment.getId(),
                comment.getContent(),
                comment.getPost().getTitle(),
                comment.getPost().getType(),
                comment.getCreatedDate());
    }
}
