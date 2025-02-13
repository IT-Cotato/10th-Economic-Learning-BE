package com.ripple.BE.user.dto;

import lombok.Builder;

@Builder
public record UserCompletedDTO(
        long userId,
        long beginnerCompletedCount,
        long intermediateCompletedCount,
        long advancedCompletedCount,
        long totalConceptCompletedCount,
        long quizCount) {}
