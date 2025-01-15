package com.ripple.BE.learning.dto;

import com.ripple.BE.learning.domain.learningset.LearningSet;
import java.util.List;
import java.util.Map;

public record LearningSetDTO(
        Long id, String name, List<ConceptDTO> conceptDTOList, List<QuizDTO> quizDTOList) {

    private static final String NAME = "name";

    public static LearningSetDTO toLearningSetDTO(final LearningSet learningSet) {
        return new LearningSetDTO(
                learningSet.getId(),
                learningSet.getName(),
                learningSet.getConcepts().stream().map(ConceptDTO::toConceptDTO).toList(),
                learningSet.getQuizzes().stream().map(QuizDTO::toQuizDTO).toList());
    }

    public static LearningSetDTO toLearningSetDTO(final Map<String, String> excelData) {
        return new LearningSetDTO(null, excelData.get(NAME), null, null);
    }
}
