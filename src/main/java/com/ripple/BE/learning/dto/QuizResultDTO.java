package com.ripple.BE.learning.dto;

public record QuizResultDTO(Boolean isCorrect, int correctCount, String explanation) {

    public static QuizResultDTO toQuizResultDTO(
            final Boolean isCorrect, final int correctCount, final String explanation) {
        return new QuizResultDTO(isCorrect, correctCount, explanation);
    }
}
