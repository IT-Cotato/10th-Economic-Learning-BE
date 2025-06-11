package com.ripple.BE.learning.dto.response.quiz;

import com.ripple.BE.learning.domain.quiz.Quiz;
import com.ripple.BE.learning.domain.type.Type;
import com.ripple.BE.user.domain.type.Level;
import java.util.List;

public record QuizResponseDTO(
        Long id,
        String learningSetName,
        Level level,
        Type type,
        String question,
        String answer,
        List<ChoiceResponseDTO> choiceList) {

    public static QuizResponseDTO from(final Quiz quiz) {
        return new QuizResponseDTO(
                quiz.getId(),
                quiz.getLearningSetName(),
                quiz.getLevel(),
                quiz.getType(),
                quiz.getQuestion(),
                quiz.getAnswer(),
                quiz.getChoices().stream().map(ChoiceResponseDTO::from).toList());
    }
}
