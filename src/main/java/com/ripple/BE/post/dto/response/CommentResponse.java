package com.ripple.BE.post.dto.response;

import com.ripple.BE.post.dto.CommentDTO;
import java.time.format.DateTimeFormatter;
import java.util.List;

public record CommentResponse(
        Long id,
        String content,
        long likeCount,
        Long commenterId,
        String commenterName,
        String commenterProfileImageUrl,
        boolean isDeleted,
        String modifiedDate,
        List<CommentResponse> children) {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); // 년-월-일 시:분

    public static CommentResponse toCommentResponse(CommentDTO commentDTO) {
        return new CommentResponse(
                commentDTO.id(),
                commentDTO.content(),
                commentDTO.likeCount(),
                commentDTO.commenter().id(),
                commentDTO.commenter().nickname(),
                commentDTO.commenter().profileImage().url(),
                commentDTO.isDeleted(),
                commentDTO.modifiedDate().format(FORMATTER),
                commentDTO.children().stream().map(CommentResponse::toCommentResponse).toList());
    }
}
