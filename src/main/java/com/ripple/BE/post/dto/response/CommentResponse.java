package com.ripple.BE.post.dto.response;

import com.ripple.BE.global.utils.RelativeTimeFormatter;
import com.ripple.BE.image.domain.Image;
import com.ripple.BE.image.domain.S3Info;
import com.ripple.BE.post.dto.CommentDTO;
import java.util.List;
import java.util.Optional;

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
                Optional.ofNullable(commentDTO.commenter().profileImage())
                        .map(Image::getS3Info)
                        .map(S3Info::getUrl)
                        .orElse(null),
                commentDTO.isDeleted(),
                commentDTO.isAuthor(),
                commentDTO.isLiked(),
                commentDTO.replyCount(),
                RelativeTimeFormatter.formatRelativeTime(commentDTO.createdDate()),
                commentDTO.children().stream().map(CommentResponse::toCommentResponse).toList());
    }
}
