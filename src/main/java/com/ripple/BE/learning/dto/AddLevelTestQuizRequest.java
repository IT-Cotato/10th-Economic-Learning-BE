package com.ripple.BE.learning.dto;

import com.ripple.BE.learning.domain.Quiz;
import com.ripple.BE.learning.domain.type.Purpose;
import com.ripple.BE.learning.domain.type.Type;
import jakarta.validation.constraints.NotNull;

public record AddLevelTestQuizRequest(
        @NotNull Type type,
        @NotNull String question,
        @NotNull String answer,
        @NotNull String wrongAnswer,
        @NotNull String explanation) {

    public Quiz toEntity() {
        return Quiz.builder()
                .purpose(Purpose.LEVEL_TEST)
                .type(type)
                .level(null)
                .question(question)
                .answer(answer)
                .wrongAnswer(wrongAnswer)
                .explanation(explanation)
                .learningSet(null)
                .build();
    }
}
