package com.ripple.BE.post.persistence.dto;

import com.ripple.BE.post.domain.type.PostType;
import java.time.LocalDateTime;

public record PostDetailDTO(
        Long postId,
        String title,
        String content,
        Long authorId,
        String authorNickname,
        String profileImageUrl,
        PostType type,
        long likeCount,
        long scrapCount,
        long commentCount,
        boolean isLiked,
        boolean isScrapped,
        boolean isAuthor,
        LocalDateTime createdDate) {}
