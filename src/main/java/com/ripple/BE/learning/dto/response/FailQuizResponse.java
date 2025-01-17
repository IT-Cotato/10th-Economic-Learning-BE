package com.ripple.BE.learning.dto.response;

import com.ripple.BE.learning.dto.QuizDTO;

public record FailQuizResponse(Long id, String name, String learningSet) {
    public static FailQuizResponse toFailQuizResponse(final QuizDTO quizDTO) {
        return new FailQuizResponse(quizDTO.id(), quizDTO.name(), quizDTO.learningSetName());
    }
}
