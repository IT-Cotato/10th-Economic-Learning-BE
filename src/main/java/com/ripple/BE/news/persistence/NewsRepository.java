package com.ripple.BE.news.persistence;

import com.ripple.BE.news.domain.News;
import com.ripple.BE.news.domain.type.NewsCategory;
import com.ripple.BE.news.domain.type.NewsSort;
import com.ripple.BE.news.persistence.dto.NewsWithScrapDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NewsRepository {

    Page<NewsWithScrapDTO> findAll(Pageable pageable, NewsSort sort, long userId); // 전체 뉴스 조회

    Page<NewsWithScrapDTO> findByCategory(
            NewsCategory category, NewsSort sort, Pageable pageable, long userId); // 특정 카테고리 뉴스 조회

    Optional<News> findByIdForUpdate(long id); // 뉴스 ID로 조회 (비관적 락)

    Optional<News> findById(long id); // 뉴스 ID로 조회

    void save(News news); // 뉴스 저장

    void saveAllNews(List<News> newsList); // 뉴스 목록 저장

    List<String> findExistingUrls(List<String> urls); // 이미 존재하는 뉴스 URL 조회

    Page<NewsWithScrapDTO> searchNews(String keyword, Pageable pageable, long userId);
}
