package com.ripple.BE.news.dto;

import com.ripple.BE.news.domain.News;
import java.util.List;
import org.springframework.data.domain.Page;

public record NewsListDTO(List<NewsDTO> newsDTOList, int totalPage, int currentPage) {

    public static NewsListDTO toNewsListDTO(Page<News> newsPage) {
        return new NewsListDTO(
                newsPage.getContent().stream().map(NewsDTO::toNewsDTO).toList(),
                newsPage.getTotalPages(),
                newsPage.getNumber());
    }

    public static NewsListDTO toNewsListDTO(List<News> newsList) {
        return new NewsListDTO(newsList.stream().map(NewsDTO::toNewsDTO).toList(), 1, 0);
    }
}
