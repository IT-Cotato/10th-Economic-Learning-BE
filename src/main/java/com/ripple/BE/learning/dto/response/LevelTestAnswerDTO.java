package com.ripple.BE.learning.dto.response;

import com.ripple.BE.learning.domain.quiz.Quiz;

public record LevelTestAnswerDTO(
        long quizId, String question, String answer, String explanation, boolean isCorrect) {
    public static LevelTestAnswerDTO toLevelTestAnswerDTO(Quiz quiz, boolean isCorrect) {
        return new LevelTestAnswerDTO(
                quiz.getId(), quiz.getQuestion(), quiz.getAnswer(), quiz.getExplanation(), isCorrect);
    }
}
