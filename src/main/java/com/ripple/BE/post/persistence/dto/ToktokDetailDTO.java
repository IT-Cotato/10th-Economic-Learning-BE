package com.ripple.BE.post.persistence.dto;

import java.time.LocalDate;

public record ToktokDetailDTO(
        String title,
        String content,
        long likeCount,
        long scrapCount,
        boolean isScraped,
        boolean isLiked,
        LocalDate usedDate) {}
