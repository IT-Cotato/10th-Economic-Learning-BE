package com.ripple.BE.learning.dto.response;

import com.ripple.BE.learning.dto.QuizListDTO;
import java.util.List;

public record ScrapQuizListResponse(List<ScrapQuizResponse> scrapQuizList) {
    public static ScrapQuizListResponse toScrapQuizListResponse(final QuizListDTO quizListDTO) {
        return new ScrapQuizListResponse(
                quizListDTO.quizList().stream().map(ScrapQuizResponse::toScrapQuizResponse).toList());
    }
}
