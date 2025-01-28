package com.ripple.BE.term.dto;

import com.ripple.BE.term.domain.Term;
import java.util.Map;

public record TermDTO(
        Long id, String title, String description, String initial, Boolean isScraped) {

    private static final String TITLE = "용어";
    private static final String DESCRIPTION = "설명";

    public static TermDTO toTermDTO(final Map<String, String> excelData) {
        return new TermDTO(null, excelData.get(TITLE), excelData.get(DESCRIPTION), null, null);
    }

    public static TermDTO toTermDTO(final Term term) {
        return new TermDTO(
                term.getId(),
                term.getTitle(),
                term.getDescription(),
                term.getInitial(),
                term.getIsScrapped());
    }
}
