package com.ripple.BE.learning.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ripple.BE.learning.dto.QuizResultDTO;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record QuizResultResponse(
        Boolean isCorrect, // 정답 여부
        int correctCount, // 정답 개수
        String explanation // 해설
        ) {

    public static QuizResultResponse toQuizResultResponse(final QuizResultDTO quizResultDTO) {
        return new QuizResultResponse(
                quizResultDTO.isCorrect(), quizResultDTO.correctCount(), quizResultDTO.explanation());
    }
}
