package com.ripple.BE.learning.dto.response.quiz;

public record QuizCompletionDTO(boolean beginner, boolean intermediate, boolean advanced) {
    public static QuizCompletionDTO of(boolean beginner, boolean intermediate, boolean advanced) {
        return new QuizCompletionDTO(beginner, intermediate, advanced);
    }
}
