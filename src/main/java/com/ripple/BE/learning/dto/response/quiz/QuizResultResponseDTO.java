package com.ripple.BE.learning.dto.response.quiz;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ripple.BE.learning.domain.quiz.Quiz;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record QuizResultResponseDTO(
        Boolean isCorrect, // 정답 여부
        String answer, // 정답
        String explanation // 해설
        ) {

    public static QuizResultResponseDTO of(final Boolean isCorrect, final Quiz quiz) {
        return new QuizResultResponseDTO(isCorrect, quiz.getAnswer(), quiz.getExplanation());
    }
}
