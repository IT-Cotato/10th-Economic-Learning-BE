package com.ripple.BE.learning.dto.response.quiz;

import com.ripple.BE.learning.domain.quiz.Quiz;

public record FailQuizResponseDTO(Long id, String name, String learningSet) {

    public static FailQuizResponseDTO from(Quiz quiz) {
        return new FailQuizResponseDTO(quiz.getId(), quiz.getName(), quiz.getLearningSetName());
    }
}
