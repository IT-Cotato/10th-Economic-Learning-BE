package com.ripple.BE.learning.dto;

import com.ripple.BE.learning.domain.Quiz;
import com.ripple.BE.learning.domain.type.Type;
import lombok.Builder;

@Builder
public record LevelTestQuizResponse(
        Long id, Type type, String question, String answer, String wrongAnswer) {
    public static LevelTestQuizResponse from(Quiz quiz) {
        return LevelTestQuizResponse.builder()
                .id(quiz.getId())
                .type(quiz.getType())
                .question(quiz.getQuestion())
                .answer(quiz.getAnswer())
                .wrongAnswer(quiz.getWrongAnswer())
                .build();
    }
}
