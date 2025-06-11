package com.ripple.BE.learning.dto.response.concept;

import com.ripple.BE.learning.domain.concept.Concept;

public record ConceptResponseDTO(
        Long conceptId, // 개념 ID
        String name, // 개념 이름
        String explanation // 개념 설명
        ) {

    public static ConceptResponseDTO from(final Concept concept) {
        return new ConceptResponseDTO(concept.getId(), concept.getName(), concept.getExplanation());
    }
}
