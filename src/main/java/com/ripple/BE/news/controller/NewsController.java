package com.ripple.BE.news.controller;

import com.ripple.BE.global.dto.response.ApiResponse;
import com.ripple.BE.news.domain.type.NewsCategory;
import com.ripple.BE.news.domain.type.NewsSort;
import com.ripple.BE.news.dto.NewsDTO;
import com.ripple.BE.news.dto.NewsListDTO;
import com.ripple.BE.news.dto.response.NewsListResponse;
import com.ripple.BE.news.dto.response.NewsResponse;
import com.ripple.BE.news.service.NewsService;
import com.ripple.BE.user.domain.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
@Tag(name = "News", description = "뉴스 API")
public class NewsController {

    private final NewsService newsService;

    @Operation(summary = "뉴스 목록 조회", description = "뉴스 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getNewsList(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @RequestParam(required = false, defaultValue = "0") @PositiveOrZero int page,
            final @RequestParam(required = false, defaultValue = "RECENT") NewsSort sort,
            final @RequestParam(required = false) NewsCategory category) {

        NewsListDTO newsListDTO = newsService.getNewsList(page, sort, category, currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(NewsListResponse.toNewsListResponse(newsListDTO)));
    }

    @Operation(summary = "뉴스 상세 조회", description = "뉴스의 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> getNews(final @PathVariable("id") long id) {

        NewsDTO newsDTO = newsService.getNews(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(NewsResponse.toNewsResponse(newsDTO)));
    }

    @Operation(summary = "뉴스 스크랩", description = "뉴스를 스크랩합니다.")
    @PostMapping("/{id}/scrap")
    public ResponseEntity<ApiResponse<Object>> scrapNews(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("id") long id) {

        newsService.addScrapToNews(id, currentUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }

    @Operation(summary = "뉴스 스크랩 취소", description = "뉴스 스크랩을 취소합니다.")
    @DeleteMapping("/{id}/scrap")
    public ResponseEntity<ApiResponse<Object>> unscrapNews(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("id") long id) {

        newsService.removeScrapFromNews(id, currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }
}
