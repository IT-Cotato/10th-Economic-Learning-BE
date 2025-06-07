package com.ripple.BE.news.persistence.news;

import com.ripple.BE.news.domain.News;
import com.ripple.BE.news.domain.type.NewsCategory;
import com.ripple.BE.news.domain.type.NewsSort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NewsRepositoryCustom {

    Page<News> findByCategory(
            NewsCategory category, NewsSort newsSort, Pageable pageable, long userId);

    Page<News> findAll(Pageable pageable, NewsSort newsSort, long userId);

    Page<News> searchNews(String keyword, Pageable pageable, long userId);
}
