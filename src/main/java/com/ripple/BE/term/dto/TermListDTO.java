package com.ripple.BE.term.dto;

import com.ripple.BE.term.domain.Term;
import java.util.List;

public record TermListDTO(List<TermDTO> termList // 용어 리스트
        ) {

    public static TermListDTO toTermListDTO(final List<Term> termList) {
        return new TermListDTO(termList.stream().map(TermDTO::toTermDTO).toList());
    }
}
