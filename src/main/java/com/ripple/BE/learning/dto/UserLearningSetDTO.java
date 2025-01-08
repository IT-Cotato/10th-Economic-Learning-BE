package com.ripple.BE.learning.dto;

import com.ripple.BE.learning.domain.UserLearningSet;

public record UserLearningSetDTO(
        Long learningSetId,
        Long userId,
        String name,
        boolean isLearningSetCompleted,
        boolean isConceptCompleted,
        boolean isQuizCompleted) {

    public static UserLearningSetDTO toUserLearningSetDTO(final UserLearningSet userLearningSet) {
        return new UserLearningSetDTO(
                userLearningSet.getLearningSet().getId(),
                userLearningSet.getUser().getId(),
                userLearningSet.getLearningSet().getName(),
                userLearningSet.isLearningSetCompleted(),
                userLearningSet.isConceptCompleted(),
                userLearningSet.isQuizCompleted());
    }
}
