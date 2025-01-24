package com.ripple.BE.news.crawler.impl;

import com.ripple.BE.news.crawler.NewsCrawler;
import com.ripple.BE.news.domain.type.NewsCategory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RealEstateNewsCrawler extends NewsCrawler {

    protected static final String URL = "https://news.naver.com/breakingnews/section/101/260";

    /**
     * 페이지 URL
     *
     * @return 부동산 페이지 URL
     */
    @Override
    public String getPageUrl() {
        return URL;
    }

    @Override
    public NewsCategory getNewsCategory() {
        return NewsCategory.REAL_ESTATE;
    }
}
