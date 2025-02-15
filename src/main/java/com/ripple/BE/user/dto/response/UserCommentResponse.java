package com.ripple.BE.user.dto.response;

import com.ripple.BE.global.utils.RelativeTimeFormatter;
import com.ripple.BE.user.dto.UserCommentDTO;

public record UserCommentResponse(
        Long commentId, String comment, String postTitle, String type, String createdDate) {
    public static UserCommentResponse toUserCommentResponse(UserCommentDTO userCommentDTO) {
        return new UserCommentResponse(
                userCommentDTO.commentId(),
                userCommentDTO.comment(),
                userCommentDTO.postTitle(),
                userCommentDTO.type().toString(),
                RelativeTimeFormatter.formatRelativeTime(userCommentDTO.createdDate()));
    }
}
