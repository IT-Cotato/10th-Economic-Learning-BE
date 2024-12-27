package com.ripple.BE.learning.dto.response;

import com.ripple.BE.learning.dto.ConceptListDTO;
import java.util.List;

public record ConceptListResponse(List<ConceptResponse> conceptList // 개념 리스트
        ) {

    public static ConceptListResponse toConceptListResponse(final ConceptListDTO conceptListDTO) {
        return new ConceptListResponse(
                conceptListDTO.conceptList().stream().map(ConceptResponse::toConceptResponse).toList());
    }
}
