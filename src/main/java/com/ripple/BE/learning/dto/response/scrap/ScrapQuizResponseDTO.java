package com.ripple.BE.learning.dto.response.scrap;

import com.ripple.BE.learning.domain.quiz.Quiz;
import com.ripple.BE.user.domain.type.Level;
import lombok.Builder;

@Builder
public record ScrapQuizResponseDTO(
        long quizId, String quizName, String learningSetName, Level level) {

    public static ScrapQuizResponseDTO from(final Quiz quiz) {
        return ScrapQuizResponseDTO.builder()
                .quizId(quiz.getId())
                .quizName(quiz.getQuestion())
                .learningSetName(quiz.getLearningSetName())
                .level(quiz.getLevel())
                .build();
    }
}
