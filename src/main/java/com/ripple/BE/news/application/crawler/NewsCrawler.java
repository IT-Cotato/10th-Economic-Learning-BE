package com.ripple.BE.news.application.crawler;

import static com.ripple.BE.news.exception.errorcode.NewsErrorCode.*;

import com.ripple.BE.news.domain.type.NewsCategory;
import com.ripple.BE.news.dto.crawler.NewsCrawlerDTO;
import com.ripple.BE.news.exception.NewsException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Slf4j
public abstract class NewsCrawler {

    // HTML 구조 내 주요 요소를 식별하기 위한 CSS 선택자 상수 정의
    protected static final String HEADLINE_LIST_SELECTOR = "ul.type06_headline li"; // 헤드라인 뉴스 리스트
    protected static final String NORMAL_LIST_SELECTOR = "ul.type06 li"; // 일반 뉴스 리스트
    protected static final String SUMMARY_SELECTOR = "span.lede"; // 뉴스 요약
    protected static final String PUBLISHER_SELECTOR = "span.writing"; // 언론사
    protected static final String DATE_SELECTOR = "span.date"; // 날짜

    /** 전체 뉴스 크롤링 실행(날짜별 & 페이지별로 순회하면서 뉴스 목록을 수집) */
    public List<NewsCrawlerDTO> crawl() {
        log.info("크롤링 시작: {} 카테고리", getNewsCategory());

        // 날짜를 포함한 기본 URL 생성
        String baseUrl = getPageUrl() + "&date=" + getToday();

        // 최종 수집된 뉴스 리스트
        List<NewsCrawlerDTO> newsList = new ArrayList<>();

        // 중복 방지를 위한 제목 저장
        Set<String> visitedTitles = new HashSet<>();

        int page = 1;

        while (true) {
            String pageUrl = baseUrl + "&page=" + page;

            try {
                // 단일 페이지에서 뉴스 기사 수집
                List<NewsCrawlerDTO> pageNews = crawlPage(pageUrl, visitedTitles);

                // 수집된 뉴스가 없다면 더 이상 페이지가 없으므로 반복 종료
                if (pageNews.isEmpty()) break;

                newsList.addAll(pageNews);
                page++;

            } catch (Exception e) {
                // 네트워크 오류, 파싱 오류 등 발생 시 예외 처리
                log.error("크롤링 중 예외 발생: {}", e.getMessage(), e);
                throw new NewsException(NEWS_INTERNAL_SERVER_ERROR);
            }
        }

        return newsList;
    }

    /** 주어진 URL의 페이지에서 뉴스 데이터를 수집 */
    private List<NewsCrawlerDTO> crawlPage(String pageUrl, Set<String> visitedTitles)
            throws Exception {

        // 페이지 HTML 문서 요청 및 파싱, 봇 차단을 피하기 위한 User-Agent 설정
        Document doc =
                Jsoup.connect(pageUrl)
                        .userAgent(
                                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36")
                        .referrer("https://www.google.com")
                        .header("Accept-Language", "ko-KR,ko;q=0.9")
                        .header("Connection", "keep-alive")
                        .timeout(10000)
                        .get();

        // 헤드라인 + 일반 뉴스 요소를 모두 병합
        Elements articles = new Elements();
        articles.addAll(doc.select(HEADLINE_LIST_SELECTOR));
        articles.addAll(doc.select(NORMAL_LIST_SELECTOR));

        // 뉴스가 없으면 빈 리스트 반환
        if (articles.isEmpty()) return Collections.emptyList();

        List<NewsCrawlerDTO> result = new ArrayList<>();

        // 새로운 기사가 있는지 여부 확인
        boolean hasNewArticle = false;

        for (Element element : articles) {

            // 각 기사 내에서 제목 링크 추출, 텍스트가 존재하는 <a> 요소만 필터링
            Element titleLink =
                    element.select("a").stream()
                            .filter(a -> !a.text().isBlank()) //
                            .findFirst()
                            .orElse(null);

            // 제목 없는 경우 스킵
            if (titleLink == null) continue;

            String title = titleLink.text().trim();

            // 이미 수집한 제목은 중복으로 간주하고 건너뜀
            if (visitedTitles.contains(title)) continue;

            String contentUrl = titleLink.absUrl("href"); // 기사 본문 URL
            String summary = extractText(element, SUMMARY_SELECTOR); // 뉴스 요약
            String publisher = extractText(element, PUBLISHER_SELECTOR); // 언론사
            String date = extractText(element, DATE_SELECTOR); // 날짜

            // DTO로 변환하여 결과 리스트에 추가
            NewsCrawlerDTO dto =
                    NewsCrawlerDTO.of(title, summary, publisher, contentUrl, getNewsCategory(), date);
            result.add(dto);

            // 중복 방지를 위해 제목 저장
            visitedTitles.add(title);
            hasNewArticle = true;
        }

        // 새로운 기사가 없는 경우 빈 리스트 반환하여 반복 종료 신호
        return hasNewArticle ? result : Collections.emptyList();
    }

    /** 지정된 선택자(selector)에 해당하는 요소의 텍스트를 안전하게 추출 */
    private String extractText(Element element, String selector) {
        Element selected = element.selectFirst(selector);
        return selected != null ? selected.text().trim() : "";
    }

    /** 오늘 날짜를 yyyyMMdd 형식으로 반환 */
    private String getToday() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    /** 카테고리별 뉴스 목록 페이지 URL 반환 */
    protected abstract String getPageUrl();

    /** 현재 크롤러가 담당하는 뉴스 카테고리 반환 */
    public abstract NewsCategory getNewsCategory();
}
