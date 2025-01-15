package com.ripple.BE.learning.dto.request;

import com.ripple.BE.learning.domain.type.Type;
import com.ripple.BE.learning.validation.ValidAnswerIndex;
import jakarta.validation.constraints.NotNull;

@ValidAnswerIndex
public record SubmitAnswerRequest(
        @NotNull Long quizId, // 퀴즈 ID
        @NotNull Type type, // 퀴즈 타입
        @NotNull int answerIndex // 답
        ) {}
