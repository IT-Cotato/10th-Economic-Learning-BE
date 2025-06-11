package com.ripple.BE.term.dto.response;

import com.ripple.BE.term.persistence.dto.TermWithScrapDTO;
import java.util.List;
import org.springframework.data.domain.Page;

public record TermListResponseDTO(
        List<TermPreviewResponseDTO> termList, int totalPage, int currentPage) {

    public static TermListResponseDTO from(Page<TermWithScrapDTO> termWithScrapDTOPage) {
        List<TermPreviewResponseDTO> termList =
                termWithScrapDTOPage.stream().map(TermPreviewResponseDTO::from).toList();
        return new TermListResponseDTO(
                termList, termWithScrapDTOPage.getTotalPages(), termWithScrapDTOPage.getNumber());
    }

    public static TermListResponseDTO from(List<TermWithScrapDTO> termWithScrapDTOList) {
        List<TermPreviewResponseDTO> termList =
                termWithScrapDTOList.stream().map(TermPreviewResponseDTO::from).toList();
        return new TermListResponseDTO(termList, 1, 0);
    }
}
