package com.ripple.BE.post.persistence.dto;

import com.ripple.BE.post.domain.type.PostType;
import java.time.LocalDateTime;

public record CommentWithPostDTO(
        Long commentId,
        String commentContent,
        String postTitle,
        PostType postType,
        LocalDateTime commentCreatedDate) {}
