package com.ripple.BE.learning.dto;

import com.ripple.BE.learning.domain.quiz.Quiz;

public record AnswerDTO(Long quizId, String question, String answer, String explanation) {

    public static AnswerDTO toanswerDTO(final Quiz quiz) {
        return new AnswerDTO(quiz.getId(), quiz.getQuestion(), quiz.getAnswer(), quiz.getExplanation());
    }
}
