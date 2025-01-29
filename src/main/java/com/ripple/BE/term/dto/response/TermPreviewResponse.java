package com.ripple.BE.term.dto.response;

import com.ripple.BE.term.dto.TermDTO;

public record TermPreviewResponse(
        Long termId, // 용어 ID
        String termName, // 용어명
        String termDescription, // 용어 설명
        Boolean isScraped) {

    public static TermPreviewResponse toTermPreviewResponse(final TermDTO termDTO) {
        return new TermPreviewResponse(
                termDTO.id(), termDTO.title(), termDTO.description(), termDTO.isScraped());
    }
}
