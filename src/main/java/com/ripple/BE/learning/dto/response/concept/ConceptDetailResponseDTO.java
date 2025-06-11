package com.ripple.BE.learning.dto.response.concept;

import com.ripple.BE.learning.domain.concept.Concept;

public record ConceptDetailResponseDTO(
        long id, String name, String explanation, String level, String learningSetName) {

    public static ConceptDetailResponseDTO from(final Concept concept) {
        return new ConceptDetailResponseDTO(
                concept.getId(),
                concept.getName(),
                concept.getExplanation(),
                concept.getLevel().name(),
                concept.getLearningSetName());
    }
}
