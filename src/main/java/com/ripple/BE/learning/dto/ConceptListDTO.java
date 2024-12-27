package com.ripple.BE.learning.dto;

import com.ripple.BE.learning.domain.Concept;
import java.util.List;

public record ConceptListDTO(List<ConceptDTO> conceptList // 개념 리스트
        ) {

    public static ConceptListDTO toConceptListDTO(final List<Concept> conceptList) {
        return new ConceptListDTO(conceptList.stream().map(ConceptDTO::toConceptDTO).toList());
    }
}
