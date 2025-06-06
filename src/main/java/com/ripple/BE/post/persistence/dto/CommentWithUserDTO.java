package com.ripple.BE.post.persistence.dto;

import com.ripple.BE.image.domain.Image;
import java.time.LocalDateTime;

public record CommentWithUserDTO(
        Long id,
        String content,
        long likeCount,
        long replyCount,
        Long parentId,
        boolean isDeleted,
        Long commenterId,
        String commenterName,
        Image profileImage,
        LocalDateTime createdDate) {}
