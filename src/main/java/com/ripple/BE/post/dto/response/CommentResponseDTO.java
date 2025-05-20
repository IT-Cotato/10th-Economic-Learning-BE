package com.ripple.BE.post.dto.response;

import com.ripple.BE.global.utils.RelativeTimeFormatter;
import com.ripple.BE.post.domain.comment.Comment;
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
            Comment comment, List<CommentResponseDTO> children, boolean isAuthor, boolean isLiked) {
        return new CommentResponseDTO(
                comment.getId(),
                comment.getContent(),
                comment.getLikeCount(),
                comment.getCommenter().getId(),
                comment.getCommenter().getNickname(),
                comment.getCommenter().getProfileImage() == null
                        ? null
                        : comment.getCommenter().getProfileImage().getS3Info().getUrl(),
                comment.isDeleted(),
                isAuthor,
                isLiked,
                comment.getReplyCount(),
                RelativeTimeFormatter.formatRelativeTime(comment.getCreatedDate()),
                children);
    }
}
