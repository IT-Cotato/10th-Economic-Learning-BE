package com.ripple.BE.post.persistence.dto;

import com.ripple.BE.post.domain.type.PostType;
import java.time.LocalDateTime;

public record PostWithImageDTO(
        Long id,
        String title,
        String content,
        PostType type,
        long likeCount,
        long commentCount,
        String imageUrl,
        LocalDateTime createdDate) {}
