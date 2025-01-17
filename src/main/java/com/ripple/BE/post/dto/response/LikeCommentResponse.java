package com.ripple.BE.post.dto.response;

import com.ripple.BE.post.domain.type.PostType;
import com.ripple.BE.post.dto.LikeCommentDTO;
import java.time.LocalDateTime;

public record LikeCommentResponse(
        Long id, String content, String postName, PostType type, LocalDateTime createdDate) {
    public static LikeCommentResponse toLikeCommentResponse(LikeCommentDTO likeCommentDTO) {
        return new LikeCommentResponse(
                likeCommentDTO.id(),
                likeCommentDTO.content(),
                likeCommentDTO.postName(),
                likeCommentDTO.type(),
                likeCommentDTO.createdDate());
    }
}
