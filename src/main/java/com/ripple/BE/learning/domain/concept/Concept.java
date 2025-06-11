package com.ripple.BE.learning.domain.concept;

import com.ripple.BE.user.domain.type.Level;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Concept {

    private final Long id;
    private final Level level;
    private final String name;
    private final String explanation;
    private final Long learningSetId;
    private final String learningSetName;

    @Builder(access = lombok.AccessLevel.PRIVATE)
    private Concept(
            Long id,
            Level level,
            String name,
            String explanation,
            Long learningSetId,
            String learningSetName) {
        this.id = id;
        this.level = level;
        this.name = name;
        this.explanation = explanation;
        this.learningSetId = learningSetId;
        this.learningSetName = learningSetName;
    }

    public static Concept withId(
            Long id,
            Level level,
            String name,
            String explanation,
            Long learningSetId,
            String learningSetName) {
        return Concept.builder()
                .id(id)
                .level(level)
                .name(name)
                .explanation(explanation)
                .learningSetId(learningSetId)
                .learningSetName(learningSetName)
                .build();
    }

    public static Concept withoutId(
            Level level, String name, String explanation, Long learningSetId, String learningSetName) {
        return Concept.builder()
                .level(level)
                .name(name)
                .explanation(explanation)
                .learningSetId(learningSetId)
                .learningSetName(learningSetName)
                .build();
    }
}
