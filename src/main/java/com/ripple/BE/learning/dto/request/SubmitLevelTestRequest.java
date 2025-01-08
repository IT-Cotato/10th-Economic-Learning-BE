package com.ripple.BE.learning.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;

public record SubmitLevelTestRequest(List<Answer> answers) {

    @Builder
    public record Answer(@NotNull Long quizId, @NotNull String answer) {}
}
