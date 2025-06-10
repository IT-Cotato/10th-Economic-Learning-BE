package com.ripple.BE.news.persistence.jpa.repository.news;

import com.ripple.BE.news.domain.type.NewsCategory;
import com.ripple.BE.news.domain.type.NewsSort;
import com.ripple.BE.news.persistence.dto.NewsWithScrapDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NewsQueryRepository {

    Page<NewsWithScrapDTO> findByCategory(
            NewsCategory category, NewsSort newsSort, Pageable pageable, long userId);

    Page<NewsWithScrapDTO> findAll(Pageable pageable, NewsSort newsSort, long userId);
}
