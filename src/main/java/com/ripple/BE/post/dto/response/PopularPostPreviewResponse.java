package com.ripple.BE.post.dto.response;

import com.ripple.BE.global.utils.RelativeTimeFormatter;
import com.ripple.BE.post.domain.type.PostType;
import com.ripple.BE.post.dto.PostDTO;

public record PopularPostPreviewResponse(
        Long id,
        String title,
        PostType type,
        long likeCount,
        long commentCount,
        String imageUrl,
        String createdDate) {

    public static PopularPostPreviewResponse toPopularPostPreviewResponse(PostDTO postDTO) {
        return new PopularPostPreviewResponse(
                postDTO.id(),
                postDTO.title(),
                postDTO.type(),
                postDTO.likeCount(),
                postDTO.commentCount(),
                postDTO.imageList().imageDTOList().isEmpty()
                        ? null
                        : postDTO.imageList().imageDTOList().get(0).url(),
                RelativeTimeFormatter.formatRelativeTime(postDTO.createdDate()));
    }
}
