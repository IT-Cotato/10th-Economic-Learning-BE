package com.ripple.BE.post.dto.response;

import com.ripple.BE.global.utils.RelativeTimeFormatter;
import com.ripple.BE.post.persistence.dto.CommentWithUserDTO;
import java.util.List;

public record CommentResponseDTO(
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
        List<CommentResponseDTO> children) {

    public static CommentResponseDTO of(
            CommentWithUserDTO dto,
            boolean isAuthor,
            boolean isLiked,
            List<CommentResponseDTO> children) {
        return new CommentResponseDTO(
                dto.id(),
                dto.content(),
                dto.likeCount(),
                dto.commenterId(),
                dto.commenterName(),
                dto.profileImage() != null ? dto.profileImage().getS3Info().getUrl() : null,
                dto.isDeleted(),
                isAuthor,
                isLiked,
                dto.replyCount(),
                RelativeTimeFormatter.formatRelativeTime(dto.createdDate()),
                children);
    }
}
