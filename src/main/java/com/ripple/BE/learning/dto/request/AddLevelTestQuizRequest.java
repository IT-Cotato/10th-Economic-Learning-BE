package com.ripple.BE.learning.dto.request;

import com.ripple.BE.learning.domain.type.Type;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record AddLevelTestQuizRequest(
        @NotNull Type type,
        @NotNull String question,
        @NotNull String answer,
        @NotNull String explanation,
        List<String> choices) {}
