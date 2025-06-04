package com.ripple.BE.learning.domain.learningset;

import com.ripple.BE.user.domain.type.Level;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserLearningSet {

    private final Long id;
    private final Long userId;
    private final Long learningSetId;
    private final String learningSetName;
    private final Level level;

    private final boolean isLearningSetCompleted;
    private final boolean isConceptCompleted;
    private final boolean isQuizCompleted;

    @Builder(access = lombok.AccessLevel.PRIVATE)
    public UserLearningSet(
            Long id,
            Long userId,
            Long learningSetId,
            Level level,
            String learningSetName,
            boolean isLearningSetCompleted,
            boolean isConceptCompleted,
            boolean isQuizCompleted) {
        this.id = id;
        this.userId = userId;
        this.learningSetId = learningSetId;
        this.learningSetName = learningSetName;
        this.level = level;
        this.isLearningSetCompleted = isLearningSetCompleted;
        this.isConceptCompleted = isConceptCompleted;
        this.isQuizCompleted = isQuizCompleted;
    }

    public static UserLearningSet withoutId(
            Long userId, Long learningSetId, String learningSetName, Level level) {
        return UserLearningSet.builder()
                .userId(userId)
                .learningSetId(learningSetId)
                .learningSetName(learningSetName)
                .level(level)
                .isLearningSetCompleted(false)
                .isConceptCompleted(false)
                .isQuizCompleted(false)
                .build();
    }

    public static UserLearningSet withId(
            Long id,
            Long userId,
            Long learningSetId,
            String learningSetName,
            Level level,
            boolean isLearningSetCompleted,
            boolean isConceptCompleted,
            boolean isQuizCompleted) {
        return UserLearningSet.builder()
                .id(id)
                .userId(userId)
                .learningSetId(learningSetId)
                .learningSetName(learningSetName)
                .level(level)
                .isLearningSetCompleted(isLearningSetCompleted)
                .isConceptCompleted(isConceptCompleted)
                .isQuizCompleted(isQuizCompleted)
                .build();
    }

    public UserLearningSet updateConceptCompleted() {
        return UserLearningSet.builder()
                .id(this.id)
                .userId(this.userId)
                .learningSetId(this.learningSetId)
                .learningSetName(this.learningSetName)
                .level(this.level)
                .isConceptCompleted(true)
                .isQuizCompleted(this.isQuizCompleted)
                .isLearningSetCompleted(this.isQuizCompleted)
                .build();
    }

    public UserLearningSet updateQuizCompleted() {
        return UserLearningSet.builder()
                .id(this.id)
                .userId(this.userId)
                .learningSetId(this.learningSetId)
                .learningSetName(this.learningSetName)
                .level(this.level)
                .isConceptCompleted(this.isConceptCompleted)
                .isQuizCompleted(true)
                .isLearningSetCompleted(this.isConceptCompleted)
                .build();
    }
}
