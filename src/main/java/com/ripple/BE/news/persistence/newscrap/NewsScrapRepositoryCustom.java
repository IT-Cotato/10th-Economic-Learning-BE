package com.ripple.BE.news.persistence.newscrap;

import com.ripple.BE.news.domain.News;
import java.util.List;

public interface NewsScrapRepositoryCustom {

    List<News> findNewsScrappedByUser(Long userId);
}
