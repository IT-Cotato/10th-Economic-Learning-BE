package com.ripple.BE.learning.dto.response;

import com.ripple.BE.learning.dto.QuizListDTO;
import java.util.List;
import lombok.Builder;

@Builder
public record LevelTestQuizListResponse(List<QuizResponse> quizList) {

    public static LevelTestQuizListResponse toLevelTestQuizListResponse(
            final QuizListDTO quizListDTO) {
        return new LevelTestQuizListResponse(
                quizListDTO.quizList().stream().map(QuizResponse::toQuizResponse).toList());
    }
}
