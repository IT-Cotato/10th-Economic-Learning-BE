package com.ripple.BE.news.dto.response;

import com.ripple.BE.news.persistence.dto.NewsWithScrapDTO;
import java.util.List;
import org.springframework.data.domain.Page;

public record NewsPreviewListResponseDTO(
        List<NewsPreviewResponseDTO> newsList, int totalPage, int currentPage) {

    public static NewsPreviewListResponseDTO from(Page<NewsWithScrapDTO> newsPage) {
        List<NewsPreviewResponseDTO> newsList =
                newsPage.getContent().stream().map(NewsPreviewResponseDTO::from).toList();
        return new NewsPreviewListResponseDTO(newsList, newsPage.getTotalPages(), newsPage.getNumber());
    }

    public static NewsPreviewListResponseDTO from(List<NewsWithScrapDTO> newsList) {
        List<NewsPreviewResponseDTO> responseList =
                newsList.stream().map(NewsPreviewResponseDTO::from).toList();
        return new NewsPreviewListResponseDTO(responseList, 1, 0);
    }
}
