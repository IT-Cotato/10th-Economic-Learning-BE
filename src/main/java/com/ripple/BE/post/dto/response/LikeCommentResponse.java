package com.ripple.BE.post.dto.response;

import com.ripple.BE.global.utils.RelativeTimeFormatter;
import com.ripple.BE.post.domain.type.PostType;
import com.ripple.BE.post.dto.LikeCommentDTO;

public record LikeCommentResponse(
        Long id, String content, String postName, PostType type, String createdDate) {
    public static LikeCommentResponse toLikeCommentResponse(LikeCommentDTO likeCommentDTO) {
        return new LikeCommentResponse(
                likeCommentDTO.id(),
                likeCommentDTO.content(),
                likeCommentDTO.postName(),
                likeCommentDTO.type(),
                RelativeTimeFormatter.formatRelativeTime(likeCommentDTO.createdDate()));
    }
}
