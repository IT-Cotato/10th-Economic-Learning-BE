package com.ripple.BE.learning.dto.response;

import com.ripple.BE.learning.dto.ConceptDTO;

public record ConceptResponse(
        Long conceptId, // 개념 ID
        String name, // 개념 이름
        String explanation // 개념 설명
        ) {

    public static ConceptResponse toConceptResponse(final ConceptDTO conceptDTO) {
        return new ConceptResponse(conceptDTO.conceptId(), conceptDTO.name(), conceptDTO.explanation());
    }
}
