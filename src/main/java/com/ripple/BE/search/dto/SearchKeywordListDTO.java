package com.ripple.BE.search.dto;

import java.util.List;

public record SearchKeywordListDTO(List<SearchKeywordDTO> searchKeywordDTOList) {

    public static SearchKeywordListDTO toSearchKeywordListDTO(List<String> searchKeywordList) {
        return new SearchKeywordListDTO(
                searchKeywordList.stream().map(SearchKeywordDTO::toSearchKeywordDTO).toList());
    }
}
