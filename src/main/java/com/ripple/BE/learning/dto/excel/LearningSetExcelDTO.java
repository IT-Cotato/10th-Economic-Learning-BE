package com.ripple.BE.learning.dto.excel;

import java.util.Map;

public record LearningSetExcelDTO(String learningSetName // 학습 세트 이름
        ) {

    private static final String NAME = "name";

    public static LearningSetExcelDTO from(final Map<String, String> excelData) {
        return new LearningSetExcelDTO(excelData.get(NAME));
    }
}
