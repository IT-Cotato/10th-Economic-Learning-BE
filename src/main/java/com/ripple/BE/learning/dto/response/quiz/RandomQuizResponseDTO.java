package com.ripple.BE.learning.dto.response.quiz;

import com.ripple.BE.learning.domain.quiz.Quiz;
import com.ripple.BE.learning.domain.type.Type;
import java.util.List;

public record RandomQuizResponseDTO(
        Long quizId, // 퀴즈 ID
        String learningSetName, // 학습 세트 이름
        String name, // 퀴즈 이름
        Type type, // 퀴즈 타입
        String question, // 문제
        List<ChoiceResponseDTO> choices // 선택지 목록
        ) {

    public static RandomQuizResponseDTO from(final Quiz quiz) {
        return new RandomQuizResponseDTO(
                quiz.getId(),
                quiz.getLearningSetName(),
                quiz.getName(),
                quiz.getType(),
                quiz.getQuestion(),
                quiz.getChoices().stream().map(ChoiceResponseDTO::from).toList());
    }
}
