package com.ripple.BE.learning.dto.response;

import com.ripple.BE.learning.dto.ConceptDTO;

public record ConceptDetailResponse(
        long id, String name, String explanation, String level, String learningSetName) {
    public static ConceptDetailResponse toConceptDetailResponse(ConceptDTO conceptDTO) {
        return new ConceptDetailResponse(
                conceptDTO.conceptId(),
                conceptDTO.name(),
                conceptDTO.explanation(),
                conceptDTO.level().name(),
                conceptDTO.learningSetName());
    }
}
