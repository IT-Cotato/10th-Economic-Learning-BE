package com.ripple.BE.learning.dto;

import com.ripple.BE.learning.domain.LearningSetComplete;

public record LearningSetCompleteDTO(
        Long learningSetId,
        Long userId,
        String name,
        boolean isLearningSetCompleted,
        boolean isConceptCompleted,
        boolean isQuizCompleted) {

    public static LearningSetCompleteDTO toLearningSetCompleteDTO(
            final LearningSetComplete learningSetComplete) {
        return new LearningSetCompleteDTO(
                learningSetComplete.getLearningSet().getId(),
                learningSetComplete.getUser().getId(),
                learningSetComplete.getLearningSet().getName(),
                learningSetComplete.isLearningSetCompleted(),
                learningSetComplete.isConceptCompleted(),
                learningSetComplete.isQuizCompleted());
    }
}
