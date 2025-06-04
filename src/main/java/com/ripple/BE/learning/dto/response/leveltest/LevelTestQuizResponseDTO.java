package com.ripple.BE.learning.dto.response.leveltest;

import com.ripple.BE.learning.domain.quiz.Quiz;
import com.ripple.BE.learning.domain.type.Type;
import com.ripple.BE.learning.dto.response.quiz.ChoiceResponseDTO;
import com.ripple.BE.user.domain.type.Level;
import java.util.List;

public record LevelTestQuizResponseDTO(
        Long id, Level level, Type type, String question, List<ChoiceResponseDTO> choiceList) {

    public static LevelTestQuizResponseDTO from(final Quiz quiz) {
        return new LevelTestQuizResponseDTO(
                quiz.getId(),
                quiz.getLevel(),
                quiz.getType(),
                quiz.getQuestion(),
                quiz.getChoices().stream().map(ChoiceResponseDTO::from).toList());
    }
}
