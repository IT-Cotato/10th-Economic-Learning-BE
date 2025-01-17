package com.ripple.BE.post.dto;

import com.ripple.BE.post.domain.Comment;
import java.util.List;

public record LikeCommentListDTO(List<LikeCommentDTO> likeCommentDTOList) {
    public static LikeCommentListDTO toLikeCommentListDTO(List<Comment> commentList) {
        return new LikeCommentListDTO(
                commentList.stream().map(LikeCommentDTO::toLikeCommentDTO).toList());
    }
}
