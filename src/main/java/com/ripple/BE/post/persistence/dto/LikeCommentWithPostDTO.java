package com.ripple.BE.post.persistence.dto;

import com.ripple.BE.post.domain.type.PostType;
import java.time.LocalDateTime;

public record LikeCommentWithPostDTO(
        Long commentId,
        String commentContent,
        LocalDateTime createdDate,
        Long postId,
        String postTitle,
        PostType postType) {}
