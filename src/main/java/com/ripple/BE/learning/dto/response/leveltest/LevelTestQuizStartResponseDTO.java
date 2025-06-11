package com.ripple.BE.learning.dto.response.leveltest;

import java.util.List;

public record LevelTestQuizStartResponseDTO(
        String anonymousKey, List<LevelTestQuizResponseDTO> quizzes) {

    public static LevelTestQuizStartResponseDTO of(
            final String anonymousKey, final List<LevelTestQuizResponseDTO> quizzes) {
        return new LevelTestQuizStartResponseDTO(anonymousKey, quizzes);
    }
}
