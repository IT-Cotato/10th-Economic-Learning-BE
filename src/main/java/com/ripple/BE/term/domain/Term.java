package com.ripple.BE.term.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Term {

    private final Long id;
    private final String title;
    private final String description;
    private final String initial;

    @Builder(access = AccessLevel.PRIVATE)
    private Term(Long id, String title, String description, String initial) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.initial = initial;
    }

    public static Term withId(Long id, String title, String description, String initial) {
        return Term.builder().id(id).title(title).description(description).initial(initial).build();
    }

    public static Term withoutId(String title, String description, String initial) {
        return Term.builder().title(title).description(description).initial(initial).build();
    }

    public Term updateInitial(String initial) {
        return Term.builder()
                .id(this.id)
                .title(this.title)
                .description(this.description)
                .initial(initial)
                .build();
    }
}
