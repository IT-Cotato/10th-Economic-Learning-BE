package com.ripple.BE.post.dto;

import com.ripple.BE.post.domain.Comment;
import java.util.List;

public record CommentListDTO(List<CommentDTO> commentDTOList) {

    public static CommentListDTO toCommentListDTO(List<Comment> commentList) {
        return new CommentListDTO(commentList.stream().map(CommentDTO::toCommentDTO).toList());
    }
}
