package com.ripple.BE.learning.dto.response.leveltest;

import com.ripple.BE.learning.domain.quiz.Quiz;
import com.ripple.BE.learning.domain.type.Type;

public record LevelTestQuizAnswerDTO(
        Long quizId, String question, String answer, String explanation, Type type) {

    public static LevelTestQuizAnswerDTO from(final Quiz quiz) {
        return new LevelTestQuizAnswerDTO(
                quiz.getId(), quiz.getQuestion(), quiz.getAnswer(), quiz.getExplanation(), quiz.getType());
    }
}
