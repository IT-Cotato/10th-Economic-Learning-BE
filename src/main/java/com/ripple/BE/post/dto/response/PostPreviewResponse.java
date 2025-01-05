package com.ripple.BE.post.dto.response;

import com.ripple.BE.global.utils.RelativeTimeFormatter;
import com.ripple.BE.post.domain.type.PostType;
import com.ripple.BE.post.dto.PostDTO;

public record PostPreviewResponse(
        Long id,
        String title,
        String content,
        PostType type,
        long likeCount,
        long commentCount,
        String imageUrl,
        String createdDate) {

    public static PostPreviewResponse toPostPreviewResponse(PostDTO postDTO) {
        return new PostPreviewResponse(
                postDTO.id(),
                postDTO.title(),
                postDTO.content(),
                postDTO.type(),
                postDTO.likeCount(),
                postDTO.commentCount(),
                postDTO.imageList().imageDTOList().isEmpty()
                        ? null
                        : postDTO.imageList().imageDTOList().get(0).url(),
                RelativeTimeFormatter.formatRelativeTime(postDTO.createdDate()));
    }
}
