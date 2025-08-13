package com.ripple.BE.news.application;

import static com.ripple.BE.news.exception.errorcode.NewsErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.ripple.BE.news.domain.News;
import com.ripple.BE.news.domain.NewsScrap;
import com.ripple.BE.news.domain.type.NewsCategory;
import com.ripple.BE.news.domain.type.NewsSort;
import com.ripple.BE.news.dto.response.NewsPreviewListResponseDTO;
import com.ripple.BE.news.dto.response.NewsResponseDTO;
import com.ripple.BE.news.exception.NewsException;
import com.ripple.BE.news.persistence.NewsRepository;
import com.ripple.BE.news.persistence.NewsScrapRepository;
import com.ripple.BE.news.persistence.dto.NewsWithScrapDTO;
import com.ripple.BE.user.service.AttendanceService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(
        properties = {
            "spring.sql.init.mode=always",
            "spring.sql.init.schema-locations=classpath:schema-test.sql"
        })
class NewsServiceTest {

    NewsService newsService;

    @Mock NewsRepository newsRepository;
    @Mock NewsScrapRepository newsScrapRepository;
    @Mock AttendanceService attendanceService;

    @BeforeEach
    void setUp() {
        newsService = new NewsService(newsRepository, newsScrapRepository, attendanceService);
    }

    private NewsWithScrapDTO createNewsDto(
            long id, String title, LocalDateTime date, long viewCount, NewsCategory category) {
        return new NewsWithScrapDTO(
                id, title, "요약", "내용", "http://example.com/news/" + id, category, viewCount, date, false);
    }

    @Test
    @DisplayName("뉴스 상세 조회 시 조회수가 1 증가하고, DTO로 반환되며 퀘스트 완료가 호출된다")
    void getNews_ShouldIncreaseViewCount_AndReturnDTO() {
        // given
        long newsId = 1L;
        long userId = 1L;
        News news =
                News.withId(
                        newsId,
                        "테스트 뉴스",
                        "내용",
                        "발행자",
                        3,
                        "http://example.com/news/1",
                        NewsCategory.ECONOMIC_ANALYSIS,
                        LocalDateTime.now());

        given(newsRepository.findByIdForUpdate(newsId)).willReturn(Optional.of(news));
        given(newsRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));

        // when
        NewsResponseDTO response = newsService.getNews(newsId, userId);

        // then
        assertThat(response.views()).isEqualTo(4);
        assertThat(response.title()).isEqualTo("테스트 뉴스");
        assertThat(response.content()).isEqualTo("내용");
        assertThat(response.publisher()).isEqualTo("발행자");

        verify(newsRepository).findByIdForUpdate(newsId);
        verify(newsRepository).save(any(News.class));
        verify(attendanceService).completeQuest(userId, "ARTICLE");
    }

    @Test
    @DisplayName("존재하지 않는 뉴스 ID로 조회 시 예외가 발생한다")
    void getNews_ShouldThrowException_WhenNewsNotFound() {
        // given
        long newsId = 1L;
        long userId = 1L;

        given(newsRepository.findByIdForUpdate(newsId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> newsService.getNews(newsId, userId))
                .isInstanceOf(NewsException.class)
                .extracting("errorCode")
                .isEqualTo(NEWS_NOT_FOUND);

        verify(newsRepository).findByIdForUpdate(newsId);
        verify(newsRepository, never()).save(any());
        verify(attendanceService, never()).completeQuest(anyLong(), anyString());
    }

    @Test
    @DisplayName("카테고리 없이 뉴스 목록 요청 시 findAll이 호출되어야 한다")
    void getNewsList_ShouldUseFindAll_WhenCategoryIsNull() {
        // given
        int page = 0;
        long userId = 1L;
        NewsSort sort = NewsSort.RECENT;
        Pageable pageable = PageRequest.of(page, 10);

        NewsWithScrapDTO news1 =
                createNewsDto(2L, "최신 뉴스", LocalDateTime.now(), 20, NewsCategory.ECONOMIC_POLICY);
        NewsWithScrapDTO news2 =
                createNewsDto(
                        1L, "오래된 뉴스", LocalDateTime.now().minusDays(2), 10, NewsCategory.ECONOMIC_ANALYSIS);

        given(newsRepository.findAll(pageable, sort, userId))
                .willReturn(new PageImpl<>(List.of(news1, news2)));

        // when
        NewsPreviewListResponseDTO response = newsService.getNewsList(page, sort, null, userId);

        // then
        assertThat(response.newsList()).hasSize(2);
        assertThat(response.newsList().get(0).title()).isEqualTo("최신 뉴스");

        verify(newsRepository).findAll(pageable, sort, userId);
        verify(newsRepository, never()).findByCategory(any(), any(), any(), anyLong());
    }

    @Test
    @DisplayName("카테고리가 존재하면 findByCategory가 호출되어야 한다")
    void getNewsList_ShouldUseFindByCategory_WhenCategoryProvided() {
        // given
        int page = 0;
        long userId = 2L;
        NewsSort sort = NewsSort.RECENT;
        NewsCategory category = NewsCategory.ECONOMIC_POLICY;
        Pageable pageable = PageRequest.of(page, 10);

        NewsWithScrapDTO news1 = createNewsDto(3L, "정책 뉴스1", LocalDateTime.now(), 30, category);
        NewsWithScrapDTO news2 =
                createNewsDto(4L, "정책 뉴스2", LocalDateTime.now().minusDays(1), 5, category);

        given(newsRepository.findByCategory(category, sort, pageable, userId))
                .willReturn(new PageImpl<>(List.of(news1, news2)));

        // when
        NewsPreviewListResponseDTO response = newsService.getNewsList(page, sort, category, userId);

        // the
        assertThat(response.newsList()).hasSize(2);
        assertThat(response.newsList().get(0).title()).isEqualTo("정책 뉴스1");

        verify(newsRepository).findByCategory(category, sort, pageable, userId);
        verify(newsRepository, never()).findAll(any(), any(), anyLong());
    }

    @Test
    @DisplayName("뉴스 스크랩 추가 시 뉴스가 존재하지 않으면 예외가 발생한다")
    void addScrap_ShouldThrow_WhenNewsNotFound() {
        // given
        long userId = 1L;
        long newsId = 100L;

        given(newsRepository.findById(newsId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> newsService.addScrapToNews(newsId, userId))
                .isInstanceOf(NewsException.class)
                .extracting("errorCode")
                .isEqualTo(NEWS_NOT_FOUND);

        verify(newsRepository).findById(newsId);
        verify(newsScrapRepository, never()).save(any());
    }

    @Test
    @DisplayName("뉴스 스크랩 추가 시 이미 스크랩된 뉴스면 예외가 발생한다")
    void addScrap_ShouldThrow_WhenAlreadyScrapped() {
        // given
        long userId = 1L;
        long newsId = 101L;

        News news =
                News.withId(
                        newsId, "제목", "내용", "발행자", 0, "url", NewsCategory.ECONOMIC_POLICY, LocalDateTime.now());
        given(newsRepository.findById(newsId)).willReturn(Optional.of(news));
        given(newsScrapRepository.existsByNewsIdAndUserId(newsId, userId)).willReturn(true);

        // when & then
        assertThatThrownBy(() -> newsService.addScrapToNews(newsId, userId))
                .isInstanceOf(NewsException.class)
                .extracting("errorCode")
                .isEqualTo(NEWS_SCRAP_ALREADY_EXIST);

        verify(newsScrapRepository, never()).save(any());
    }

    @Test
    @DisplayName("뉴스 스크랩 추가 시 유효한 경우 스크랩이 저장되어야 한다")
    void addScrap_ShouldSave_WhenValid() {
        // given
        long userId = 1L;
        long newsId = 102L;

        News news =
                News.withId(
                        newsId, "제목", "내용", "발행자", 0, "url", NewsCategory.ECONOMIC_POLICY, LocalDateTime.now());

        given(newsRepository.findById(newsId)).willReturn(Optional.of(news));
        given(newsScrapRepository.existsByNewsIdAndUserId(newsId, userId)).willReturn(false);

        // when
        newsService.addScrapToNews(newsId, userId);

        // then
        verify(newsScrapRepository).save(any(NewsScrap.class));
    }

    @Test
    @DisplayName("뉴스 스크랩 삭제 시 스크랩이 존재하지 않으면 예외가 발생한다")
    void removeScrap_ShouldThrow_WhenNotFound() {
        // given
        long userId = 1L;
        long newsId = 103L;

        given(newsScrapRepository.findByNewsIdAndUserId(newsId, userId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> newsService.removeScrapFromNews(newsId, userId))
                .isInstanceOf(NewsException.class)
                .extracting("errorCode")
                .isEqualTo(NEWS_SCRAP_NOT_FOUND);

        verify(newsScrapRepository, never()).delete(any());
    }

    @Test
    @DisplayName("뉴스 스크랩 삭제 시 스크랩이 존재하면 삭제된다")
    void removeScrap_ShouldDelete_WhenExists() {
        // given
        long userId = 1L;
        long newsId = 104L;

        NewsScrap scrap = NewsScrap.withId(1L, userId, newsId);
        given(newsScrapRepository.findByNewsIdAndUserId(newsId, userId)).willReturn(Optional.of(scrap));

        // when
        newsService.removeScrapFromNews(newsId, userId);

        // then
        verify(newsScrapRepository).delete(scrap);
    }
}
