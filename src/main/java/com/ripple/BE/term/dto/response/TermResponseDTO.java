package com.ripple.BE.term.dto.response;

import com.ripple.BE.term.domain.Term;

public record TermResponseDTO(
        Long termId, // 용어 ID
        String termName, // 용어명
        String termDescription // 용어 설명
        ) {

    public static TermResponseDTO from(Term term) {
        return new TermResponseDTO(term.getId(), term.getTitle(), term.getDescription());
    }
}
