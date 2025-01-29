package com.ripple.BE.learning.dto.response;

import com.ripple.BE.learning.domain.type.Type;
import com.ripple.BE.learning.dto.QuizDTO;
import com.ripple.BE.user.domain.type.Level;

public record QuizResponse(
        Long id,
        String learningSetName,
        Level level,
        Type type,
        String question,
        String answer,
        ChoiceListResponse choiceList) {
    public static QuizResponse toQuizResponse(final QuizDTO quizDTO) {
        return new QuizResponse(
                quizDTO.id(),
                quizDTO.learningSetName(),
                quizDTO.level(),
                quizDTO.type(),
                quizDTO.question(),
                quizDTO.answer(),
                ChoiceListResponse.toChoiceListResponse(quizDTO.choiceList()));
    }
}
