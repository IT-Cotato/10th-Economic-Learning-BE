package com.ripple.BE.news.persistence.jpa.repository.newsscrap;

import com.ripple.BE.news.persistence.dto.NewsWithScrapDTO;
import java.util.List;

public interface NewsScrapQueryRepository {

    List<NewsWithScrapDTO> findNewsScrappedByUser(Long userId);
}
