package com.ripple.BE.learning.dto;

import static com.ripple.BE.learning.dto.LearningSetDTO.*;

import com.ripple.BE.learning.domain.Concept;
import com.ripple.BE.user.domain.type.Level;
import java.util.Map;

public record ConceptDTO(
        Long conceptId, String name, String explanation, Level level, String learningSetName) {

    private static final String NAME = "name";
    private static final String EXPLANATION = "explanation";
    private static final String LEVEL = "level";
    private static final String LEARNING_SET_NAME = "learning_set_name";

    public static ConceptDTO toConceptDTO(final Concept concept) {
        return new ConceptDTO(
                concept.getConceptId(),
                concept.getName(),
                concept.getExplanation(),
                concept.getLevel(),
                null);
    }

    public static ConceptDTO toConceptDTO(final Map<String, String> excelData) {
        return new ConceptDTO(
                null,
                excelData.get(NAME),
                excelData.get(EXPLANATION),
                Level.valueOf(excelData.get(LEVEL)),
                excelData.get(LEARNING_SET_NAME));
    }
}
