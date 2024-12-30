package com.ripple.BE.learning.dto.response;

import com.ripple.BE.learning.dto.QuizListDTO;
import java.util.List;

public record QuizListResponse(List<QuizResponse> quizList) {

    public static QuizListResponse toQuizListResponse(final QuizListDTO quizListDTO) {
        return new QuizListResponse(
                quizListDTO.quizList().stream().map(QuizResponse::toQuizResponse).toList());
    }
}
