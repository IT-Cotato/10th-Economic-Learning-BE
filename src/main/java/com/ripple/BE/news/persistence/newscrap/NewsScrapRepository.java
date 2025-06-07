package com.ripple.BE.news.persistence.newscrap;

import com.ripple.BE.news.domain.NewsScrap;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsScrapRepository
        extends JpaRepository<NewsScrap, Long>, NewsScrapRepositoryCustom {

    Optional<NewsScrap> findByNewsIdAndUserId(long newsId, long userId);

    boolean existsByNewsIdAndUserId(long newsId, long userId);
}
