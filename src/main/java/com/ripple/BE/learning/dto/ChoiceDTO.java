package com.ripple.BE.learning.dto;

import com.ripple.BE.learning.domain.quiz.Choice;

public record ChoiceDTO(Long id, String content) {

    public static ChoiceDTO toChoiceDTO(final Choice choice) {
        return new ChoiceDTO(choice.getId(), choice.getContent());
    }

    public static ChoiceDTO toChoiceDTO(final String content) {
        return new ChoiceDTO(null, content);
    }
}
