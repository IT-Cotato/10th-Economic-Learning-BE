package com.ripple.BE.learning.dto;

public record QuizResultDTO(Boolean isCorrect, String explanation) {

    public static QuizResultDTO toQuizResultDTO(final Boolean isCorrect, final String explanation) {
        return new QuizResultDTO(isCorrect, explanation);
    }
}
