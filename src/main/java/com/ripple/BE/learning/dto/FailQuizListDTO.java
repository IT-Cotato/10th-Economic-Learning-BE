package com.ripple.BE.learning.dto;

import com.ripple.BE.learning.domain.quiz.Quiz;
import java.util.List;

public record FailQuizListDTO(List<QuizDTO> failQuizList) {

    public static FailQuizListDTO toFailQuizListDTO(final List<Quiz> quizList) {
        return new FailQuizListDTO(quizList.stream().map(QuizDTO::toFailQuizDTO).toList());
    }
}
