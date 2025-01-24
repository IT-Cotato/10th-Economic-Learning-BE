package com.ripple.BE.term.dto.response;

import com.ripple.BE.term.dto.TermListDTO;
import java.util.List;

public record TermListResponse(List<TermResponse> termList, int totalPage, int currentPage) {

    public static TermListResponse toTermListResponse(TermListDTO termListDTO) {
        return new TermListResponse(
                termListDTO.termList().stream().map(TermResponse::toTermResponse).toList(),
                termListDTO.totalPage(),
                termListDTO.currentPage());
    }
}
