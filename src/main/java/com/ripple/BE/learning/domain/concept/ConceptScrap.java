package com.ripple.BE.learning.domain.concept;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ConceptScrap {

    private final Long id;
    private final Long userId;
    private final Long conceptId;

    @Builder(access = lombok.AccessLevel.PRIVATE)
    private ConceptScrap(Long id, Long userId, Long conceptId) {
        this.id = id;
        this.userId = userId;
        this.conceptId = conceptId;
    }

    public static ConceptScrap withoutId(Long userId, Long conceptId) {
        return ConceptScrap.builder().userId(userId).conceptId(conceptId).build();
    }

    public static ConceptScrap withId(Long id, Long userId, Long conceptId) {
        return ConceptScrap.builder().id(id).userId(userId).conceptId(conceptId).build();
    }
}
