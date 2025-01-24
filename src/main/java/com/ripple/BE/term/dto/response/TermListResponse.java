package com.ripple.BE.term.dto.response;

import com.ripple.BE.term.dto.TermListDTO;
import java.util.List;

public record TermListResponse(List<TermResponse> termList // 용어 리스트
        ) {

    public static TermListResponse toTermListResponse(final TermListDTO termListDTO) {
        return new TermListResponse(
                termListDTO.termList().stream().map(TermResponse::toTermResponse).toList());
    }
}
