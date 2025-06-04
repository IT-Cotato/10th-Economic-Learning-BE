package com.ripple.BE.learning.dto.response.quiz;

import com.ripple.BE.learning.domain.quiz.Choice;

public record ChoiceResponseDTO(String content // 선택지 내용
        ) {

    public static ChoiceResponseDTO from(Choice choice) {
        return new ChoiceResponseDTO(choice.getContent());
    }
}
