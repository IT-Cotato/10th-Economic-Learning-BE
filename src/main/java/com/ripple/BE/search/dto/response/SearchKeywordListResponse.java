package com.ripple.BE.search.dto.response;

import com.ripple.BE.search.dto.SearchKeywordListDTO;
import java.util.List;

public record SearchKeywordListResponse(List<SearchKeywordResponse> searchKeywords) {

    public static SearchKeywordListResponse toSearchKeywordListResponse(
            SearchKeywordListDTO searchKeywordListDTO) {
        return new SearchKeywordListResponse(
                searchKeywordListDTO.searchKeywordDTOList().stream()
                        .map(SearchKeywordResponse::toSearchKeywordResponse)
                        .toList());
    }
}
