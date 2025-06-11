package com.ripple.BE.term.dto.excel;

import java.util.Map;

public record TermExcelDTO(String title, String description) {

    private static final String TITLE = "용어";
    private static final String DESCRIPTION = "설명";

    public static TermExcelDTO from(final Map<String, String> excelData) {
        return new TermExcelDTO(excelData.get(TITLE), excelData.get(DESCRIPTION));
    }
}
