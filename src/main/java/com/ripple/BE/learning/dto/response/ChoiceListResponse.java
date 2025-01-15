package com.ripple.BE.learning.dto.response;

import com.ripple.BE.learning.dto.ChoiceListDTO;
import java.util.List;

public record ChoiceListResponse(List<ChoiceResponse> choiceList // 선택지 목록
        ) {

    public static ChoiceListResponse toChoiceListResponse(final ChoiceListDTO choiceListDTO) {
        return new ChoiceListResponse(
                choiceListDTO.choices().stream().map(ChoiceResponse::toChoiceResponse).toList());
    }
}
