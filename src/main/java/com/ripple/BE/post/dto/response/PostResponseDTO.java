package com.ripple.BE.post.dto.response;

import com.ripple.BE.global.utils.RelativeTimeFormatter;
import com.ripple.BE.image.dto.response.ImageResponse;
import com.ripple.BE.post.domain.type.PostType;
import com.ripple.BE.post.persistence.dto.PostDetailDTO;
import java.util.List;

public record PostResponseDTO(
        String title,
        String author,
        Long authorId,
        String authorProfileImage,
        String content,
        PostType type,
        long likeCount,
        long commentCount,
        long scrapCount,
        boolean isScraped,
        boolean isLiked,
        boolean isAuthor,
        List<ImageResponse> imageList,
        List<CommentResponseDTO> commentList,
        String createdDate) {

    public static PostResponseDTO of(
            PostDetailDTO dto, List<ImageResponse> imageList, List<CommentResponseDTO> commentList) {
        return new PostResponseDTO(
                dto.title(),
                dto.authorNickname(),
                dto.authorId(),
                dto.profileImageUrl(),
                dto.content(),
                dto.type(),
                dto.likeCount(),
                dto.commentCount(),
                dto.scrapCount(),
                dto.isScrapped(),
                dto.isLiked(),
                dto.isAuthor(),
                imageList,
                commentList,
                RelativeTimeFormatter.formatRelativeTime(dto.createdDate()));
    }
}
