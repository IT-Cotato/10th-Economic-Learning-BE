package com.ripple.BE.post.dto.response;

import com.ripple.BE.post.domain.type.PostType;
import com.ripple.BE.post.dto.PostDTO;
import java.time.format.DateTimeFormatter;

public record PostPreviewResponse(
        Long id,
        String title,
        String content,
        PostType type,
        long likeCount,
        long commentCount,
        String imageUrl,
        String createdDate) {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); // 년-월-일 시:분

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
                postDTO.createdDate().format(FORMATTER));
    }
}
