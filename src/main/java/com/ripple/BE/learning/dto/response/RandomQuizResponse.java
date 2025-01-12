package com.ripple.BE.learning.dto.response;

import com.ripple.BE.learning.domain.type.Type;
import com.ripple.BE.learning.dto.QuizDTO;

public record RandomQuizResponse(
        Long quizId, // 퀴즈 ID
        String name, // 퀴즈 이름
        Type type, // 퀴즈 타입
        String question, // 문제
        ChoiceListResponse choiceList // 선택지 목록
        ) {

    public static RandomQuizResponse toQuizResponse(final QuizDTO quizDTO) {

        return new RandomQuizResponse(
                quizDTO.id(),
                quizDTO.name(),
                quizDTO.type(),
                quizDTO.question(),
                ChoiceListResponse.toChoiceListResponse(quizDTO.choiceList()));
    }
}
