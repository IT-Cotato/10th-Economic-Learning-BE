package com.ripple.BE.learning.application.quiz.command;

import com.ripple.BE.learning.dto.request.SubmitLevelTestRequest;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;

public record SubmitLevelTestCommand(List<AnswerCommand> answers) {

    @Builder
    public record AnswerCommand(@NotNull Long quizId, @NotNull String answer) {}

    public static SubmitLevelTestCommand from(SubmitLevelTestRequest request) {
        return new SubmitLevelTestCommand(
                request.answers().stream()
                        .map(answer -> new AnswerCommand(answer.quizId(), answer.answer()))
                        .toList());
    }
}
