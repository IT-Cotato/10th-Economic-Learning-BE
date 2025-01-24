package com.ripple.BE.news.crawler;

import static com.ripple.BE.news.exception.errorcode.NewsErrorCode.*;

import com.ripple.BE.news.domain.type.NewsCategory;
import com.ripple.BE.news.dto.NewsDTO;
import com.ripple.BE.news.exception.NewsException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Slf4j
public abstract class NewsCrawler {

    protected static final String LIST_SELECTOR = "li.sa_item"; // 리스트 선택자
    protected static final String TITLE_SELECTOR = "strong.sa_text_strong"; // 제목 선택자
    protected static final String CONTENT_SELECTOR = "div.sa_text_lede"; // 내용 선택자
    protected static final String PUBLISH_SELECTOR = "div.sa_text_press"; // 언론사 선택자
    protected static final String CONTENT_URL_SELECTOR = "div.sa_text a"; // 본문 URL 선택자

    public List<NewsDTO> crawl() {

        log.info("Crawling started");

        String baseUrl = getPageUrl(); // 크롤링할 URL
        List<NewsDTO> newsList = new ArrayList<>();

        try {

            Elements elementList = Jsoup.connect(baseUrl).get().select(LIST_SELECTOR);
            if (elementList.isEmpty()) {
                return newsList;
            }

            for (Element element : elementList) {
                String title = element.select(TITLE_SELECTOR).text();
                String content = element.select(CONTENT_SELECTOR).text();
                String publisher = element.select(PUBLISH_SELECTOR).text();
                String contentUrl = element.select(CONTENT_URL_SELECTOR).attr("href");

                if (!title.isEmpty() && !content.isEmpty()) {
                    NewsDTO newsDTO =
                            NewsDTO.toNewsDTO(title, content, publisher, contentUrl, getNewsCategory());
                    newsList.add(newsDTO);
                }
            }

        } catch (Exception e) {
            throw new NewsException(NEWS_INTERNAL_SERVER_ERROR);
        }

        return newsList;
    }

    /**
     * 크롤링할 페이지 URL 반환
     *
     * @return 페이지 URL
     */
    protected abstract String getPageUrl();

    /** 카테고리 반환 */
    protected abstract NewsCategory getNewsCategory();
}
