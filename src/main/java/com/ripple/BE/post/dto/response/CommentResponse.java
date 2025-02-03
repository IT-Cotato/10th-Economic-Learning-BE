package com.ripple.BE.post.dto.response;

import com.ripple.BE.global.utils.RelativeTimeFormatter;
import com.ripple.BE.post.dto.CommentDTO;
import java.util.List;

public record CommentResponse(
        Long id,
        String content,
        long likeCount,
        Long commenterId,
        String commenterName,
        String commenterProfileImageUrl,
        boolean isDeleted,
        boolean isAuthor,
        boolean isLiked,
        long replyCount,
        String createdDate,
        List<CommentResponse> children) {

    public static CommentResponse toCommentResponse(CommentDTO commentDTO) {
        return new CommentResponse(
                commentDTO.id(),
                commentDTO.content(),
                commentDTO.likeCount(),
                commentDTO.commenter().id(),
                commentDTO.commenter().nickname(),
                commentDTO.commenter().profileImage().url(),
                commentDTO.isDeleted(),
                commentDTO.isAuthor(),
                commentDTO.isLiked(),
                commentDTO.replyCount(),
                RelativeTimeFormatter.formatRelativeTime(commentDTO.createdDate()),
                commentDTO.children().stream().map(CommentResponse::toCommentResponse).toList());
    }
}
