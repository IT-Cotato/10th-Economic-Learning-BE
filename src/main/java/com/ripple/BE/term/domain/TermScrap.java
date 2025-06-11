package com.ripple.BE.term.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TermScrap {

    private final Long id;
    private final Long userId;
    private final Long termId;

    @Builder(access = AccessLevel.PRIVATE)
    private TermScrap(Long id, Long userId, Long termId) {
        this.id = id;
        this.userId = userId;
        this.termId = termId;
    }

    public static TermScrap withId(Long id, Long userId, Long termId) {
        return TermScrap.builder().id(id).userId(userId).termId(termId).build();
    }

    public static TermScrap withoutId(Long userId, Long termId) {
        return TermScrap.builder().userId(userId).termId(termId).build();
    }
}
