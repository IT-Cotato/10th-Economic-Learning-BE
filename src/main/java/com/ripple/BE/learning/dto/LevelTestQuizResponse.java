package com.ripple.BE.learning.dto;

import com.ripple.BE.learning.domain.Quiz;
import lombok.Builder;

@Builder
public record LevelTestQuizResponse(Long id, String question, String answer, String wrongAnswer) {
    public static LevelTestQuizResponse from(Quiz quiz) {
        return LevelTestQuizResponse.builder()
                .id(quiz.getId())
                .question(quiz.getQuestion())
                .answer(quiz.getAnswer())
                .wrongAnswer(quiz.getWrongAnswer())
                .build();
    }
}
