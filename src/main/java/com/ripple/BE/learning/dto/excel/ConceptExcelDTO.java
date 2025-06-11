package com.ripple.BE.learning.dto.excel;

import com.ripple.BE.user.domain.type.Level;
import java.util.Map;

public record ConceptExcelDTO(
        String name, // 개념 이름
        String explanation, // 개념 설명
        Level level, // 개념 레벨
        String learningSetName // 학습 세트 이름
        ) {
    private static final String NAME = "name";
    private static final String EXPLANATION = "explanation";
    private static final String LEVEL = "level";
    private static final String LEARNING_SET_NAME = "learning_set_name";

    public static ConceptExcelDTO from(final Map<String, String> excelData) {
        return new ConceptExcelDTO(
                excelData.get(NAME),
                excelData.get(EXPLANATION),
                Level.valueOf(excelData.get(LEVEL)),
                excelData.get(LEARNING_SET_NAME));
    }
}
