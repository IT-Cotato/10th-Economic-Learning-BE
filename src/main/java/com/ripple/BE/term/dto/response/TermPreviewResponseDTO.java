package com.ripple.BE.term.dto.response;

import com.ripple.BE.term.persistence.dto.TermWithScrapDTO;
import com.ripple.BE.term.persistence.jpa.entity.TermJpaEntity;

public record TermPreviewResponseDTO(
        Long termId, // 용어 ID
        String termName, // 용어명
        String termDescription, // 용어 설명
        Boolean isScraped) {

    public static TermPreviewResponseDTO from(TermWithScrapDTO termWithScrapDTO) {

        return new TermPreviewResponseDTO(
                termWithScrapDTO.id(),
                termWithScrapDTO.title(),
                termWithScrapDTO.description(),
                termWithScrapDTO.isScrapped());
    }

    public static TermPreviewResponseDTO from(TermJpaEntity termJpaEntity) {
        return new TermPreviewResponseDTO(
                termJpaEntity.getId(),
                termJpaEntity.getTitle(),
                termJpaEntity.getDescription(),
                false); // isScraped는 false로 설정
    }
}
