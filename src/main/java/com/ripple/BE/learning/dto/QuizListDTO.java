package com.ripple.BE.learning.dto;

import com.ripple.BE.learning.domain.Quiz;
import java.util.List;

public record QuizListDTO(List<QuizDTO> quizList) {

    public static QuizListDTO toQuizListDTO(final List<Quiz> quizList) {
        return new QuizListDTO(quizList.stream().map(QuizDTO::toQuizDTO).toList());
    }

    public static QuizListDTO fromQuidDTOList(final List<QuizDTO> quizList) {
        return new QuizListDTO(quizList);
    }
}
