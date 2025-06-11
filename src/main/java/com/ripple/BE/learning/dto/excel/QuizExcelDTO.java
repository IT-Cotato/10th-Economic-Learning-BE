package com.ripple.BE.learning.dto.excel;

import com.ripple.BE.learning.domain.type.Type;
import com.ripple.BE.user.domain.type.Level;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public record QuizExcelDTO(
        String name,
        Type type,
        Level level,
        String question,
        String answer,
        List<ChoiceExcelDTO> choices,
        String explanation,
        String learningSetName) {

    public record ChoiceExcelDTO(String content) {}

    private static final String NAME = "name";
    private static final String TYPE = "type";
    private static final String LEVEL = "level";
    private static final String QUESTION = "question";
    private static final String ANSWER = "answer";
    private static final String EXPLANATION = "explanation";
    private static final String LEARNING_SET_NAME = "learning_set_name";
    private static final String CHOICES = "choices";

    public static QuizExcelDTO from(Map<String, String> excelData) {
        List<ChoiceExcelDTO> choices =
                Arrays.stream(excelData.get(CHOICES).split("\\d+\\.\\s*")) // "1. 보기", "2. 보기" 구분
                        .filter(choice -> !choice.trim().isEmpty())
                        .map(choice -> new ChoiceExcelDTO(choice.trim()))
                        .toList();

        return new QuizExcelDTO(
                excelData.get(NAME),
                Type.valueOf(excelData.get(TYPE)),
                Level.valueOf(excelData.get(LEVEL)),
                excelData.get(QUESTION),
                excelData.get(ANSWER),
                choices,
                excelData.get(EXPLANATION),
                excelData.get(LEARNING_SET_NAME));
    }
}
