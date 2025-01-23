package com.ripple.BE.learning.dto.response;

import com.ripple.BE.learning.dto.ConceptListDTO;
import java.util.List;

public record ScrapConceptListResponse(List<ScrapConceptResponse> scrapConceptList) {
    public static ScrapConceptListResponse toScrapConceptListResponse(ConceptListDTO conceptListDTO) {
        return new ScrapConceptListResponse(
                conceptListDTO.conceptList().stream()
                        .map(ScrapConceptResponse::toScrapConceptResponse)
                        .toList());
    }
}
