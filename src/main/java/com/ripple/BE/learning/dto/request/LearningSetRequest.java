package com.ripple.BE.learning.dto.request;

import com.ripple.BE.user.domain.type.Level;
import jakarta.validation.constraints.NotNull;

public record LearningSetRequest(@NotNull Level level) {}
