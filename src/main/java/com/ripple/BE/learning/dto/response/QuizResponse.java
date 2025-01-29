package com.ripple.BE.learning.dto.response;

import com.ripple.BE.learning.domain.type.Type;
import com.ripple.BE.learning.dto.QuizDTO;

public record QuizResponse(
        Long id,
        String learningSetName,
        Type type,
        String question,
        String answer,
        ChoiceListResponse choiceList) {
    public static QuizResponse toQuizResponse(final QuizDTO quizDTO) {
        return new QuizResponse(
                quizDTO.id(),
                quizDTO.learningSetName(),
                quizDTO.type(),
                quizDTO.question(),
                quizDTO.answer(),
                ChoiceListResponse.toChoiceListResponse(quizDTO.choiceList()));
    }
}
