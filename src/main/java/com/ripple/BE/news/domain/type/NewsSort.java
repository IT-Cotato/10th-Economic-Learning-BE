package com.ripple.BE.news.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NewsSort {
    RECENT("최신순"),
    POPULAR("인기순");

    private final String newsSort;
}
