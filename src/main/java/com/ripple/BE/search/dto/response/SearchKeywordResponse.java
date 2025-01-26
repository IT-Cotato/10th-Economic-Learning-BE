package com.ripple.BE.search.dto.response;

import com.ripple.BE.search.dto.SearchKeywordDTO;

public record SearchKeywordResponse(String keyword) {
    public static SearchKeywordResponse toSearchKeywordResponse(
            final SearchKeywordDTO searchKeywordDTO) {
        return new SearchKeywordResponse(searchKeywordDTO.keyword());
    }
}
