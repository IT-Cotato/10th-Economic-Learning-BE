package com.ripple.BE.post.persistence.dto;

import com.ripple.BE.post.domain.type.PostType;
import java.time.LocalDate;

public record ToktokWithScrapAndImageDTO(
        Long id,
        String title,
        String content,
        PostType type,
        long likeCount,
        long commentCount,
        long scrapCount,
        String imageUrl,
        boolean isScraped,
        LocalDate usedDate) {}
