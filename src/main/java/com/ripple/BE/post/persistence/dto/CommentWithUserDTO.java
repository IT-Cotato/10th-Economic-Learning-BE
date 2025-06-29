package com.ripple.BE.post.persistence.dto;

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
        String profileImageUrl,
        LocalDateTime createdDate) {}
