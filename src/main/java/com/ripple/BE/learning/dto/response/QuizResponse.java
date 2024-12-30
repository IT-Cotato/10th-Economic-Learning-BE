package com.ripple.BE.learning.dto.response;

import com.ripple.BE.learning.domain.type.Type;
import com.ripple.BE.learning.dto.QuizDTO;
import java.util.List;

public record QuizResponse(
        Long quizId, // 퀴즈 ID
        Type type, // 퀴즈 타입
        String question, // 문제
        List<String> options // 보기, 선지
        ) {

    public static QuizResponse toQuizResponse(final QuizDTO quizDTO) {

        return new QuizResponse(quizDTO.id(), quizDTO.type(), quizDTO.question(), quizDTO.options());
    }
}
