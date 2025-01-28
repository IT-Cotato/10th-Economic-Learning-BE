package com.ripple.BE.term.dto.response;

import com.ripple.BE.term.dto.TermListDTO;
import java.util.List;

public record TermListResponse(List<TermPreviewResponse> termList, int totalPage, int currentPage) {

    public static TermListResponse toTermListResponse(TermListDTO termListDTO) {
        return new TermListResponse(
                termListDTO.termList().stream().map(TermPreviewResponse::toTermPreviewResponse).toList(),
                termListDTO.totalPage(),
                termListDTO.currentPage());
    }
}
