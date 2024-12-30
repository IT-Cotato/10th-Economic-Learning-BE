package com.ripple.BE.learning.dto;

import com.ripple.BE.learning.dto.request.SubmitLevelTestRequest;
import java.util.List;

public record QuizSubmitDTO(List<Answer> answers) {

    public record Answer(Long quizId, String answer) {}

    public static QuizSubmitDTO toQuizDto(SubmitLevelTestRequest submitLevelTestRequest) {
        return new QuizSubmitDTO(
                submitLevelTestRequest.answers().stream()
                        .map(answer -> new Answer(answer.quizId(), answer.answer()))
                        .toList());
    }
}
