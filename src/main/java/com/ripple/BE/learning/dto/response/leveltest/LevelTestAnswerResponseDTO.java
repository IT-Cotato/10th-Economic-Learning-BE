package com.ripple.BE.learning.dto.response.leveltest;

public record LevelTestAnswerResponseDTO(
        long quizId, String question, String answer, String explanation, boolean isCorrect) {

    public static LevelTestAnswerResponseDTO of(
            final LevelTestQuizAnswerDTO levelTestQuizAnswerDTO, final boolean isCorrect) {
        return new LevelTestAnswerResponseDTO(
                levelTestQuizAnswerDTO.quizId(),
                levelTestQuizAnswerDTO.question(),
                levelTestQuizAnswerDTO.answer(),
                levelTestQuizAnswerDTO.explanation(),
                isCorrect);
    }
}
