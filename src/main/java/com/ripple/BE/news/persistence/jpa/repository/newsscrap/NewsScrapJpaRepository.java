package com.ripple.BE.news.persistence.jpa.repository.newsscrap;

import com.ripple.BE.news.persistence.jpa.entity.NewsScrapJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsScrapJpaRepository
        extends JpaRepository<NewsScrapJpaEntity, Long>, NewsScrapQueryRepository {

    Optional<NewsScrapJpaEntity> findByNewsIdAndUserId(long newsId, long userId);

    boolean existsByNewsIdAndUserId(long newsId, long userId);

    void deleteAllByUserId(long userId);
}
