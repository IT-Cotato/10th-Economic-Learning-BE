package com.ripple.BE.news.persistence.impl;

import com.ripple.BE.news.domain.NewsScrap;
import com.ripple.BE.news.persistence.NewsScrapRepository;
import com.ripple.BE.news.persistence.dto.NewsWithScrapDTO;
import com.ripple.BE.news.persistence.jpa.entity.NewsScrapJpaEntity;
import com.ripple.BE.news.persistence.jpa.repository.newsscrap.NewsScrapJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NewsScrapRepositoryImpl implements NewsScrapRepository {

    private final NewsScrapJpaRepository newsScrapJpaRepository;

    @Override
    public boolean existsByNewsIdAndUserId(long newsId, long userId) {
        return newsScrapJpaRepository.existsByNewsIdAndUserId(newsId, userId);
    }

    @Override
    public void save(NewsScrap newsScrap) {
        newsScrapJpaRepository.save(NewsScrapJpaEntity.from(newsScrap));
    }

    @Override
    public Optional<NewsScrap> findByNewsIdAndUserId(long newsId, long userId) {
        return newsScrapJpaRepository
                .findByNewsIdAndUserId(newsId, userId)
                .map(NewsScrapJpaEntity::toModel);
    }

    @Override
    public void delete(NewsScrap newsScrap) {
        newsScrapJpaRepository.delete(NewsScrapJpaEntity.from(newsScrap));
    }

    @Override
    public List<NewsWithScrapDTO> findNewsScrappedByUser(long userId) {
        return newsScrapJpaRepository.findNewsScrappedByUser(userId);
    }
}
