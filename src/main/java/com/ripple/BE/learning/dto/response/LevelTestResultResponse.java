package com.ripple.BE.learning.dto.response;

import com.ripple.BE.learning.dto.AnswerDTO;
import com.ripple.BE.user.domain.type.Level;
import java.util.List;
import lombok.Builder;

@Builder
public record LevelTestResultResponse(
        Integer correctCount, Level level, List<AnswerDTO> answerResponses) {

    public static LevelTestResultResponse toQuizResponse(
            final Integer correctCount, final Level level, final List<AnswerDTO> answerResponses) {
        return new LevelTestResultResponse(correctCount, level, answerResponses);
    }
}
