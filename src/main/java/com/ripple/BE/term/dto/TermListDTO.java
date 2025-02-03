package com.ripple.BE.term.dto;

import com.ripple.BE.term.domain.Term;
import java.util.List;
import org.springframework.data.domain.Page;

public record TermListDTO(List<TermDTO> termList, int totalPage, int currentPage // 용어 리스트
        ) {

    public static TermListDTO toTermListDTO(Page<Term> termPage) {
        return new TermListDTO(
                termPage.getContent().stream().map(TermDTO::toTermDTO).toList(),
                termPage.getTotalPages(),
                termPage.getNumber());
    }

    public static TermListDTO toTermListDTO(List<Term> termList) {
        return new TermListDTO(termList.stream().map(TermDTO::toTermDTO).toList(), 1, 0);
    }
}
