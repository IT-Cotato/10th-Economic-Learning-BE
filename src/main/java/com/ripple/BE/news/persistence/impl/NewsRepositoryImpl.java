package com.ripple.BE.news.persistence.impl;

import com.ripple.BE.news.domain.News;
import com.ripple.BE.news.domain.type.NewsCategory;
import com.ripple.BE.news.domain.type.NewsSort;
import com.ripple.BE.news.persistence.NewsRepository;
import com.ripple.BE.news.persistence.dto.NewsWithScrapDTO;
import com.ripple.BE.news.persistence.jdbc.NewsJdbcRepository;
import com.ripple.BE.news.persistence.jpa.entity.NewsJpaEntity;
import com.ripple.BE.news.persistence.jpa.repository.news.NewsJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NewsRepositoryImpl implements NewsRepository {

    private final NewsJpaRepository newsJpaRepository;
    private final NewsJdbcRepository newsJdbcRepository;

    @Override
    public Page<NewsWithScrapDTO> findAll(
            final Pageable pageable, final NewsSort sort, final long userId) {
        return newsJpaRepository.findAll(pageable, sort, userId);
    }

    @Override
    public Page<NewsWithScrapDTO> findByCategory(
            final NewsCategory category,
            final NewsSort sort,
            final Pageable pageable,
            final long userId) {
        return newsJpaRepository.findByCategory(category, sort, pageable, userId);
    }

    @Override
    public Optional<News> findByIdForUpdate(final long id) {
        return newsJpaRepository.findByIdForUpdate(id).map(NewsJpaEntity::toModel);
    }

    @Override
    public Optional<News> findById(final long id) {
        return newsJpaRepository.findById(id).map(NewsJpaEntity::toModel);
    }

    @Override
    public void save(final News news) {
        newsJpaRepository.save(NewsJpaEntity.from(news));
    }

    @Override
    public void saveAllNews(final List<News> newsList) {
        newsJdbcRepository.saveAllNewsByJdbcTemplate(
                newsList.stream().map(NewsJpaEntity::from).toList());
    }

    @Override
    public List<String> findExistingUrls(final List<String> urls) {
        return newsJdbcRepository.findExistingUrls(urls);
    }

    @Override
    public Page<NewsWithScrapDTO> searchNews(
            final String keyword, final Pageable pageable, final long userId) {
        return newsJdbcRepository.searchNews(keyword, pageable, userId);
    }
}
