package com.ripple.BE.learning.dto.response.scrap;

import com.ripple.BE.learning.domain.concept.Concept;
import com.ripple.BE.user.domain.type.Level;
import lombok.Builder;

@Builder
public record ScrapConceptResponseDTO(long id, String name, String LearningSetName, Level level) {

    public static ScrapConceptResponseDTO from(final Concept concept) {
        return ScrapConceptResponseDTO.builder()
                .id(concept.getId())
                .name(concept.getName())
                .LearningSetName(concept.getLearningSetName())
                .level(concept.getLevel())
                .build();
    }
}
