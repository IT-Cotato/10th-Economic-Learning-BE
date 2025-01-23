package com.ripple.BE.learning.dto.response;

import com.ripple.BE.learning.dto.ConceptDTO;
import com.ripple.BE.user.domain.type.Level;
import lombok.Builder;

@Builder
public record ScrapConceptResponse(long id, String name, String LearningSetName, Level level) {
    public static ScrapConceptResponse toScrapConceptResponse(ConceptDTO conceptDTO) {
        return ScrapConceptResponse.builder()
                .id(conceptDTO.conceptId())
                .name(conceptDTO.name())
                .LearningSetName(conceptDTO.learningSetName())
                .level(conceptDTO.level())
                .build();
    }
}
