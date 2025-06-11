package com.ripple.BE.learning.domain.quiz;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Choice {

    private final Long id;
    private final String content;

    @Builder(access = AccessLevel.PRIVATE)
    private Choice(Long id, String content) {
        this.id = id;
        this.content = content;
    }

    public static Choice withId(Long id, String content) {
        return Choice.builder().id(id).content(content).build();
    }

    public static Choice withoutId(String content) {
        return Choice.builder().content(content).build();
    }
}
