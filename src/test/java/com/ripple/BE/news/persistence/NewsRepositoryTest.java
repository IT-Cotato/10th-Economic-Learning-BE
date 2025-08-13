package com.ripple.BE.news.persistence;

import static org.junit.jupiter.api.Assertions.*;

import com.ripple.BE.global.config.JpaConfig;
import com.ripple.BE.global.config.QuerydslConfig;
import com.ripple.BE.news.domain.News;
import com.ripple.BE.news.domain.NewsScrap;
import com.ripple.BE.news.domain.type.NewsCategory;
import com.ripple.BE.news.domain.type.NewsSort;
import com.ripple.BE.news.persistence.dto.NewsWithScrapDTO;
import com.ripple.BE.news.persistence.impl.NewsRepositoryImpl;
import com.ripple.BE.news.persistence.impl.NewsScrapRepositoryImpl;
import com.ripple.BE.news.persistence.jdbc.NewsJdbcRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@Import({
    NewsRepositoryImpl.class,
    NewsJdbcRepository.class,
    NewsScrapRepositoryImpl.class,
    QuerydslConfig.class,
    JpaConfig.class
})
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(
        properties = {
            "spring.sql.init.mode=always",
            "spring.sql.init.schema-locations=classpath:schema-test.sql"
        })
class NewsRepositoryTest {

    @Autowired private NewsRepository newsRepository;
    @Autowired private NewsScrapRepository newsScrapRepository;
    @Autowired private EntityManager entityManager;

    private static final Pageable DEFAULT_PAGE = Pageable.ofSize(10);
    private static final long USER_ID = 1L;

    private News createNews(
            String title, int views, String url, NewsCategory category, LocalDateTime pubDate) {
        return News.withoutId(title, "내용", "리플", views, url, category, pubDate);
    }

    private News createAndSaveNews(String title) {
        return newsRepository.save(
                createNews(title, 0, title + ".com", NewsCategory.ECONOMIC_ANALYSIS, LocalDateTime.now()));
    }

    @Test
    @DisplayName("뉴스 저장 후 ID가 생성되고 데이터가 일치해야 한다")
    void saveNews() {
        // given
        News news1 = createNews("뉴스1", 0, "url1", NewsCategory.ECONOMIC_ANALYSIS, LocalDateTime.now());
        News news2 = createNews("뉴스2", 0, "url2", NewsCategory.ECONOMIC_ANALYSIS, LocalDateTime.now());

        // when
        News saved1 = newsRepository.save(news1);
        News saved2 = newsRepository.save(news2);

        // then
        assertAll(
                () -> assertNotNull(saved1.getId()),
                () -> assertNotNull(saved2.getId()),
                () -> assertEquals("뉴스1", saved1.getTitle()),
                () -> assertEquals("뉴스2", saved2.getTitle()));
    }

    @Test
    @DisplayName("뉴스 ID로 조회 시 데이터가 일치해야 한다")
    void findById() {
        // given
        News saved = createAndSaveNews("뉴스");

        // when
        Optional<News> found = newsRepository.findById(saved.getId());

        // then
        assertTrue(found.isPresent());
        assertEquals(saved.getTitle(), found.get().getTitle());
    }

    @Test
    @DisplayName("존재하지 않는 ID로 조회하면 Optional.empty()를 반환해야 한다")
    void findById_NotFound() {
        assertTrue(newsRepository.findById(999L).isEmpty());
    }

    @Test
    @DisplayName("뉴스 ID로 비관적 락 조회 시 데이터가 일치해야 한다")
    void findByIdForUpdate() {
        // given
        News saved = createAndSaveNews("뉴스 락 테스트");
        entityManager.flush();
        entityManager.clear();

        // when
        Optional<News> found = newsRepository.findByIdForUpdate(saved.getId());

        // then
        assertTrue(found.isPresent());
        assertEquals(saved.getTitle(), found.get().getTitle());
    }

    @Test
    @DisplayName("비관적 락 조회 시 존재하지 않는 ID면 빈 Optional을 반환해야 한다")
    void findByIdForUpdate_NotFound() {
        assertTrue(newsRepository.findByIdForUpdate(999L).isEmpty());
    }

    @Test
    @DisplayName("뉴스 목록을 saveAllNews로 저장하면 모두 저장되어야 한다")
    void saveAllNewsTest() {
        // given
        News news1 =
                createNews(
                        "배치 저장 뉴스1", 0, "batch-url1", NewsCategory.ECONOMIC_ANALYSIS, LocalDateTime.now());
        News news2 =
                createNews(
                        "배치 저장 뉴스2", 0, "batch-url2", NewsCategory.ECONOMIC_ANALYSIS, LocalDateTime.now());

        // when
        newsRepository.saveAllNews(List.of(news1, news2));

        // then
        List<String> urls = List.of("batch-url1", "batch-url2");
        List<String> existingUrls = newsRepository.findExistingUrls(urls);

        assertEquals(2, existingUrls.size());
        assertTrue(existingUrls.containsAll(urls));
    }

    @Test
    @DisplayName("최신순으로 뉴스 전체 조회 시 최신 뉴스가 먼저 와야 한다")
    void findAll_SortedByRecent() {
        // given
        News old =
                createNews(
                        "오래된 뉴스",
                        0,
                        "url-old",
                        NewsCategory.ECONOMIC_ANALYSIS,
                        LocalDateTime.now().minusDays(1));
        News recent =
                createNews("최신 뉴스", 0, "url-new", NewsCategory.ECONOMIC_ANALYSIS, LocalDateTime.now());
        News veryOld =
                createNews(
                        "아주 오래된 뉴스",
                        0,
                        "url-very-old",
                        NewsCategory.ECONOMIC_ANALYSIS,
                        LocalDateTime.now().minusDays(2));
        newsRepository.saveAllNews(List.of(old, recent, veryOld));

        // when
        Page<NewsWithScrapDTO> result = newsRepository.findAll(DEFAULT_PAGE, NewsSort.RECENT, USER_ID);

        // then
        assertEquals("최신 뉴스", result.getContent().get(0).title());
        assertEquals("오래된 뉴스", result.getContent().get(1).title());
        assertEquals("아주 오래된 뉴스", result.getContent().get(2).title());
    }

    @Test
    @DisplayName("조회수순으로 오늘 뉴스만 조회되어야 하며, 조회수가 높은 뉴스가 먼저 나와야 한다")
    void findAll_SortedByView_TodayOnly() {
        // given
        LocalDateTime now = LocalDateTime.now();

        News high = createNews("조회수 많음", 100, "url1", NewsCategory.ECONOMIC_ANALYSIS, now);
        News low = createNews("조회수 적음", 1, "url2", NewsCategory.ECONOMIC_ANALYSIS, now);
        News yesterday =
                createNews("어제 뉴스", 999, "url3", NewsCategory.ECONOMIC_ANALYSIS, now.minusDays(1));
        newsRepository.saveAllNews(List.of(high, low, yesterday));

        // when
        Page<NewsWithScrapDTO> result = newsRepository.findAll(DEFAULT_PAGE, NewsSort.POPULAR, USER_ID);

        // then
        assertEquals(2, result.getContent().size());
        assertEquals("조회수 많음", result.getContent().get(0).title());
    }

    @Test
    @DisplayName("카테고리별 최신순 조회는 최신 뉴스가 먼저 나와야 한다")
    void findByCategory_SortedByRecent() {
        // given
        NewsCategory category = NewsCategory.ECONOMIC_POLICY;
        NewsCategory otherCategory = NewsCategory.ECONOMIC_ANALYSIS;

        News old = createNews("오래된", 0, "old-url", category, LocalDateTime.now().minusDays(1));
        News recent = createNews("최신", 0, "new-url", category, LocalDateTime.now());
        News other = createNews("다른 카테고리", 0, "other-url", otherCategory, LocalDateTime.now());
        newsRepository.saveAllNews(List.of(old, recent, other));

        // when
        Page<NewsWithScrapDTO> result =
                newsRepository.findByCategory(category, NewsSort.RECENT, DEFAULT_PAGE, USER_ID);

        // then
        assertEquals(2, result.getContent().size());
        assertEquals("최신", result.getContent().get(0).title());
    }

    @Test
    @DisplayName("카테고리별 조회수순 조회 시 오늘 뉴스만 포함되어야 한다")
    void findByCategory_SortedByView_TodayOnly() {
        // given
        NewsCategory category = NewsCategory.ECONOMIC_ANALYSIS;

        News high = createNews("오늘 조회수 많음", 50, "urlA", category, LocalDateTime.now());
        News low = createNews("오늘 조회수 적음", 5, "urlB", category, LocalDateTime.now());
        News old = createNews("어제 뉴스", 999, "urlC", category, LocalDateTime.now().minusDays(1));
        News otherCategory =
                createNews("다른 카테고리 뉴스", 1000, "urlD", NewsCategory.ECONOMIC_POLICY, LocalDateTime.now());

        newsRepository.saveAllNews(List.of(high, low, old, otherCategory));

        // when
        Page<NewsWithScrapDTO> result =
                newsRepository.findByCategory(category, NewsSort.POPULAR, DEFAULT_PAGE, USER_ID);

        // then
        assertEquals(2, result.getContent().size());
        assertEquals("오늘 조회수 많음", result.getContent().get(0).title());
    }

    @Test
    @DisplayName("뉴스 전체 조회 시 페이징과 정렬이 적용되어야 한다")
    void findAll_WithPagingAndSorting() {
        // given
        for (int i = 0; i < 15; i++) {
            newsRepository.save(
                    createNews("뉴스" + i, i, "url" + i, NewsCategory.ECONOMIC_ANALYSIS, LocalDateTime.now()));
        }

        // when
        Page<NewsWithScrapDTO> result =
                newsRepository.findAll(Pageable.ofSize(10).withPage(1), NewsSort.RECENT, USER_ID);

        // then
        assertEquals(5, result.getContent().size());
        assertEquals(15, result.getTotalElements());
    }

    @Test
    @DisplayName("카테고리별 뉴스 조회 시 페이징이 정상 동작해야 한다")
    void findByCategory_WithPaging() {
        // given
        NewsCategory category = NewsCategory.ECONOMIC_POLICY;

        for (int i = 0; i < 13; i++) {
            newsRepository.save(createNews("뉴스" + i, i, "url" + i, category, LocalDateTime.now()));
        }

        // when
        Page<NewsWithScrapDTO> page =
                newsRepository.findByCategory(
                        category, NewsSort.RECENT, Pageable.ofSize(10).withPage(1), USER_ID);

        // then
        assertEquals(3, page.getContent().size());
        assertEquals(13, page.getTotalElements());
    }

    @Test
    @DisplayName("이미 존재하는 뉴스 URL만 반환되어야 한다")
    void findExistingUrls() {
        // given
        News news1 = createNews("뉴스1", 0, "url1", NewsCategory.ECONOMIC_ANALYSIS, LocalDateTime.now());
        News news2 = createNews("뉴스2", 0, "url2", NewsCategory.ECONOMIC_ANALYSIS, LocalDateTime.now());
        News news3 = createNews("뉴스3", 0, "url3", NewsCategory.ECONOMIC_ANALYSIS, LocalDateTime.now());

        newsRepository.saveAllNews(List.of(news1, news2));

        // when
        List<String> existingUrls = newsRepository.findExistingUrls(List.of("url1", "url2", "url3"));

        // then
        assertEquals(2, existingUrls.size());
        assertTrue(existingUrls.contains(news1.getUrl()));
        assertTrue(existingUrls.contains(news2.getUrl()));
        assertFalse(existingUrls.contains(news3.getUrl()));
    }

    @Test
    @DisplayName("스크랩한 뉴스는 isScrapped = true로 반환되어야 한다. 스크랩 하지 않은 뉴스는 false로 반환되어야 한다")
    void isScrapedValue_ShouldBeTrue_WhenNewsIsScrapped() {
        // given
        News news1 =
                createNews("스크랩 뉴스", 0, "scrap-url", NewsCategory.ECONOMIC_ANALYSIS, LocalDateTime.now());
        News news2 =
                createNews(
                        "스크랩하지 않은 뉴스", 0, "unscrap-url", NewsCategory.ECONOMIC_ANALYSIS, LocalDateTime.now());
        News savedNews1 = newsRepository.save(news1);
        newsRepository.save(news2);

        newsScrapRepository.save(NewsScrap.withoutId(USER_ID, savedNews1.getId()));

        // when
        Page<NewsWithScrapDTO> result = newsRepository.findAll(DEFAULT_PAGE, NewsSort.RECENT, USER_ID);

        // then
        result
                .getContent()
                .forEach(
                        news -> {
                            if (news.id().equals(savedNews1.getId())) {
                                assertTrue(news.isScrapped());
                            } else {
                                assertFalse(news.isScrapped());
                            }
                        });
    }
}
