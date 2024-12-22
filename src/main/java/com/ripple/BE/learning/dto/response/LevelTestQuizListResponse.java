package com.ripple.BE.learning.dto.response;

import com.ripple.BE.learning.domain.Quiz;
import com.ripple.BE.learning.domain.type.Type;
import lombok.Builder;

@Builder
public record LevelTestQuizListResponse(
        Long id, Type type, String question, String answer, String wrongAnswer) {
    public static LevelTestQuizListResponse from(Quiz quiz) {
        return LevelTestQuizListResponse.builder()
                .id(quiz.getId())
                .type(quiz.getType())
                .question(quiz.getQuestion())
                .answer(quiz.getAnswer())
                .wrongAnswer(quiz.getWrongAnswer())
                .build();
    }
}
