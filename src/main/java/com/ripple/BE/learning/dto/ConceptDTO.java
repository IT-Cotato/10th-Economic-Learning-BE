package com.ripple.BE.learning.dto;

import com.ripple.BE.learning.domain.Concept;
import java.util.Map;

public record ConceptDTO(
        Long conceptId, String name, String explanation, String example, String learningSetNum) {

    private static final String NAME = "name";
    private static final String EXPLANATION = "explanation";
    private static final String EXAMPLE = "example";
    private static final String LEARNING_SET_NUM = "learning_set_num";

    public static ConceptDTO toConceptDTO(final Concept concept) {
        return new ConceptDTO(
                concept.getConceptId(),
                concept.getName(),
                concept.getExplanation(),
                concept.getExample(),
                null);
    }

    public static ConceptDTO toConceptDTO(final Map<String, String> excelData) {
        return new ConceptDTO(
                null,
                excelData.get(NAME),
                excelData.get(EXPLANATION),
                excelData.get(EXAMPLE),
                excelData.get(LEARNING_SET_NUM));
    }
}
