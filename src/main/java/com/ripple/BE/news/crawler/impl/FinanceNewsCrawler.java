package com.ripple.BE.news.crawler.impl;

import com.ripple.BE.news.crawler.NewsCrawler;
import com.ripple.BE.news.domain.type.NewsCategory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FinanceNewsCrawler extends NewsCrawler {

    protected static final String URL = "https://news.naver.com/breakingnews/section/101/259";

    /**
     * 페이지 URL
     *
     * @return 금융 페이지 URL
     */
    @Override
    public String getPageUrl() {
        return URL;
    }

    /** 카테고리 */
    @Override
    public NewsCategory getNewsCategory() {
        return NewsCategory.FINANCE;
    }
}
