package com.ripple.BE.news.dto.response;

import com.ripple.BE.news.dto.NewsListDTO;
import java.util.List;

public record NewsListResponse(List<NewsPreviewResponse> newsList, int totalPage, int currentPage) {

    public static NewsListResponse toNewsListResponse(NewsListDTO newsListDTO) {
        return new NewsListResponse(
                newsListDTO.newsDTOList().stream().map(NewsPreviewResponse::toNewsPreviewResponse).toList(),
                newsListDTO.totalPage(),
                newsListDTO.currentPage());
    }
}
