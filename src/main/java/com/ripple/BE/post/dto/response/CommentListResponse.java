package com.ripple.BE.post.dto.response;

import com.ripple.BE.post.dto.CommentListDTO;
import java.util.List;

public record CommentListResponse(List<CommentResponse> commentResponseList) {

    public static CommentListResponse toCommentListResponse(CommentListDTO commentListDTO) {
        return new CommentListResponse(
                commentListDTO.commentDTOList().stream().map(CommentResponse::toCommentResponse).toList());
    }
}
