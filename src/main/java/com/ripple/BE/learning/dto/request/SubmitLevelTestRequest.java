package com.ripple.BE.learning.dto.request;

import jakarta.validation.constraints.NotNull;

public record SubmitLevelTestRequest(@NotNull Long quizId, @NotNull String answer) {}
