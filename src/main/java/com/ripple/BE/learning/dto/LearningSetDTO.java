package com.ripple.BE.learning.dto;

import com.ripple.BE.learning.domain.LearningSet;
import com.ripple.BE.user.domain.type.Level;
import java.util.List;
import java.util.Map;

public record LearningSetDTO(
        Long id,
        String name,
        String description,
        Level level,
        List<ConceptDTO> conceptDTOList,
        List<QuizDTO> quizDTOList,
        String learningSetNum) {

    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String LEVEL = "level";
    private static final String NUMBER = "learning_set_num";

    public static LearningSetDTO toLearningSetDTO(final LearningSet learningSet) {
        return new LearningSetDTO(
                learningSet.getId(),
                learningSet.getName(),
                learningSet.getDescription(),
                learningSet.getLevel(),
                learningSet.getConcepts().stream().map(ConceptDTO::toConceptDTO).toList(),
                learningSet.getQuizzes().stream().map(QuizDTO::toQuizDTO).toList(),
                null);
    }

    public static LearningSetDTO toLearningSetDTO(final Map<String, String> excelData) {
        return new LearningSetDTO(
                null,
                excelData.get(NAME),
                excelData.get(DESCRIPTION),
                Level.valueOf(excelData.get(LEVEL)),
                null,
                null,
                excelData.get(NUMBER));
    }
}
