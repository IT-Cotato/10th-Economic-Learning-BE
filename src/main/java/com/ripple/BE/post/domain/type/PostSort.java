package com.ripple.BE.post.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostSort {
    RECENT("최신순"),
    POPULAR("인기순");

    private final String postSort;
}
