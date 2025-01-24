package com.ripple.BE.term.dto.response;

import com.ripple.BE.term.dto.TermDTO;

public record TermResponse(
        Long termId, // 용어 ID
        String termName, // 용어명
        String termDescription // 용어 설명
        ) {

    public static TermResponse toTermResponse(final TermDTO termDTO) {
        return new TermResponse(termDTO.id(), termDTO.title(), termDTO.description());
    }
}
