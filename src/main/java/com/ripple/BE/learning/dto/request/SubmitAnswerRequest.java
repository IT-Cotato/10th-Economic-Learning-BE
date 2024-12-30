package com.ripple.BE.learning.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SubmitAnswerRequest(
        @NotNull Long quizId, // 퀴즈 ID
        @Max(3) @Min(0) @NotNull int answerIndex // 답
        ) {}
