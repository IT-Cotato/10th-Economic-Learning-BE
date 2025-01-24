package com.ripple.BE.news.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NewsCategory {
    FINANCE("금융"),
    INVESTMENT("투자"),
    NORMAL("경제 일반"),
    GLOBAL("국제 경제"),
    INDUSTRY("산업"),
    REAL_ESTATE("부동산"),
    ECONOMIC_ANALYSIS("경기 분석"),
    ECONOMIC_POLICY("경제 정책"),
    OTHER("기타");

    private final String newsCategory;
}
