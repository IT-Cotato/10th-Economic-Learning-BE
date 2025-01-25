package com.ripple.BE.post.dto.response;

import com.ripple.BE.post.dto.LikeCommentListDTO;
import java.util.List;

public record LikeCommentListResponse(List<LikeCommentResponse> likeCommentResponses) {
    public static LikeCommentListResponse toLikeCommentListResponse(
            LikeCommentListDTO likeCommentListDTO) {
        return new LikeCommentListResponse(
                likeCommentListDTO.likeCommentDTOList().stream()
                        .map(LikeCommentResponse::toLikeCommentResponse)
                        .toList());
    }
}
