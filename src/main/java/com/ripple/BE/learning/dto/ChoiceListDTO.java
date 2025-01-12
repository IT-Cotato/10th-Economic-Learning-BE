package com.ripple.BE.learning.dto;

import com.ripple.BE.learning.domain.quiz.Choice;
import com.ripple.BE.learning.dto.request.AddLevelTestQuizRequest;
import java.util.List;

public record ChoiceListDTO(List<ChoiceDTO> choices) {

    public static ChoiceListDTO toChoiceListDTO(final List<Choice> choices) {
        return new ChoiceListDTO(choices.stream().map(ChoiceDTO::toChoiceDTO).toList());
    }

    public static ChoiceListDTO toChoiceListDTO(final AddLevelTestQuizRequest request) {
        return new ChoiceListDTO(request.choices().stream().map(ChoiceDTO::toChoiceDTO).toList());
    }
}
