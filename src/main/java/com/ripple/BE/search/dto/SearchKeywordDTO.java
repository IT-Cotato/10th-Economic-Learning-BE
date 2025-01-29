package com.ripple.BE.search.dto;

public record SearchKeywordDTO(String keyword) {

    public static SearchKeywordDTO toSearchKeywordDTO(final String keyword) {
        return new SearchKeywordDTO(keyword);
    }
}
