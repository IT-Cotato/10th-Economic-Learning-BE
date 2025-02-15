package com.ripple.BE.user.dto.response;

import com.ripple.BE.user.dto.UserCommentListDTO;
import java.util.List;

public record UserCommentListResponse(List<UserCommentResponse> userCommentList) {
    public static UserCommentListResponse toUserCommentListResponse(
            UserCommentListDTO userCommentListDTO) {
        return new UserCommentListResponse(
                userCommentListDTO.userCommentDTOList().stream()
                        .map(UserCommentResponse::toUserCommentResponse)
                        .toList());
    }
}
