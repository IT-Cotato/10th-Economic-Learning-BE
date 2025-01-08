package com.ripple.BE.learning.dto.response;

import com.ripple.BE.learning.domain.type.Type;
import com.ripple.BE.learning.dto.QuizDTO;

public record QuizResponse(Long id, Type type, String question, String answer, String wrongAnswer) {
    public static QuizResponse toQuizResponse(final QuizDTO quizDTO) {
        return new QuizResponse(
                quizDTO.id(), quizDTO.type(), quizDTO.question(), quizDTO.answer(), quizDTO.wrongAnswer());
    }
}
