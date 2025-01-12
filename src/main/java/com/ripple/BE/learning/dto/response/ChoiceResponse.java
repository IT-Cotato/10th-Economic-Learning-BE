package com.ripple.BE.learning.dto.response;

import com.ripple.BE.learning.dto.ChoiceDTO;

public record ChoiceResponse(
        Long choiceId, // 선택지 ID
        String content // 선택지 내용
        ) {

    public static ChoiceResponse toChoiceResponse(final ChoiceDTO choiceDTO) {
        return new ChoiceResponse(choiceDTO.id(), choiceDTO.content());
    }
}
