package com.ripple.BE.learning.dto.response.leveltest;

import com.ripple.BE.user.domain.type.Level;
import java.util.List;

public record LevelTestResultResponseDTO(
        Integer correctCount, Level level, List<LevelTestAnswerResponseDTO> answerResponses) {

    public static LevelTestResultResponseDTO toQuizResponse(
            final Integer correctCount,
            final Level level,
            final List<LevelTestAnswerResponseDTO> answerResponses) {
        return new LevelTestResultResponseDTO(correctCount, level, answerResponses);
    }
}
