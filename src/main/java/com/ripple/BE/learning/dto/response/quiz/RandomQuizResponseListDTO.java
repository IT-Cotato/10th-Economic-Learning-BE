package com.ripple.BE.learning.dto.response.quiz;

import java.util.List;

public record RandomQuizResponseListDTO(List<RandomQuizResponseDTO> quizzes) {

    public static RandomQuizResponseListDTO from(final List<RandomQuizResponseDTO> quizzes) {
        return new RandomQuizResponseListDTO(quizzes);
    }
}
