package com.ripple.BE.learning.domain.learningset;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LearningSet {

    private final Long id;
    private final String name;

    @Builder(access = AccessLevel.PRIVATE)
    public LearningSet(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static LearningSet withoutId(String name) {
        return LearningSet.builder().name(name).build();
    }

    public static LearningSet withId(Long id, String name) {
        return LearningSet.builder().id(id).name(name).build();
    }
}
