package com.ripple.BE.search.controller;

import com.ripple.BE.global.dto.response.ApiResponse;
import com.ripple.BE.news.dto.NewsListDTO;
import com.ripple.BE.news.dto.response.NewsListResponse;
import com.ripple.BE.post.dto.response.PostPreviewListResponseDTO;
import com.ripple.BE.post.dto.response.ToktokPreviewListResponseDTO;
import com.ripple.BE.post.service.PostQueryUseCase;
import com.ripple.BE.post.service.ToktokQueryUseCase;
import com.ripple.BE.search.dto.SearchKeywordListDTO;
import com.ripple.BE.search.dto.response.SearchKeywordListResponse;
import com.ripple.BE.search.service.SearchService;
import com.ripple.BE.term.dto.TermListDTO;
import com.ripple.BE.term.dto.response.TermListResponse;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/search")
@Tag(name = "Search", description = "검색 API")
public class SearchController {

    private final SearchService searchService;
    private final PostQueryUseCase postQueryUseCase;
    private final ToktokQueryUseCase toktokQueryUseCase;

    @Operation(
            summary = "일반 게시글 검색",
            description =
                    "일반 게시글을 검색합니다. 검색어가 없을 경우 전체 게시글을 조회합니다. 페이지 번호는 0부터 시작하며, 페이지 당 10개의 게시글을 반환합니다.")
    @GetMapping("/posts")
    public ResponseEntity<ApiResponse<Object>> searchPosts(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @RequestParam(value = "keyword", required = false) String keyword,
            final @RequestParam(value = "page", defaultValue = "0") @PositiveOrZero int page) {

        PostPreviewListResponseDTO postPreviewListResponseDTO =
                postQueryUseCase.searchPosts(keyword, page, currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(postPreviewListResponseDTO));
    }

    @Operation(
            summary = "톡톡 게시글 검색",
            description =
                    "톡톡 게시글을 검색합니다. 검색어가 없을 경우 전체 게시글을 조회합니다. 페이지 번호는 0부터 시작하며, 페이지 당 10개의 게시글을 반환합니다.")
    @GetMapping("/toktoks")
    public ResponseEntity<ApiResponse<Object>> searchToktoks(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @RequestParam(value = "keyword", required = false) String keyword,
            final @RequestParam(value = "page", defaultValue = "0") @PositiveOrZero int page) {

        ToktokPreviewListResponseDTO toktokPreviewListResponseDTO =
                toktokQueryUseCase.searchToktoks(keyword, page, currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(toktokPreviewListResponseDTO));
    }

    @Operation(
            summary = "뉴스 검색",
            description = "뉴스를 검색합니다. 검색어가 없을 경우 전체 뉴스를 조회합니다. 페이지 번호는 0부터 시작하며, 페이지 당 10개의 뉴스를 반환합니다.")
    @GetMapping("/news")
    public ResponseEntity<ApiResponse<Object>> searchNews(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @RequestParam(value = "keyword", required = false) String keyword,
            final @RequestParam(value = "page", defaultValue = "0") @PositiveOrZero int page) {

        NewsListDTO newsListDTO = searchService.searchNews(keyword, page, currentUser.getId());
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(NewsListResponse.toNewsListResponse(newsListDTO)));
    }

    @Operation(
            summary = "용어 검색",
            description = "용어를 검색합니다. 검색어가 없을 경우 전체 용어를 조회합니다. 페이지 번호는 0부터 시작하며, 페이지 당 10개의 용어를 반환합니다.")
    @GetMapping("/terms")
    public ResponseEntity<ApiResponse<Object>> searchTerms(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @RequestParam(value = "keyword", required = false) String keyword,
            final @RequestParam(value = "page", defaultValue = "0") @PositiveOrZero int page) {

        TermListDTO termListDTO = searchService.searchTerms(keyword, page, currentUser.getId());
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(TermListResponse.toTermListResponse(termListDTO)));
    }

    @Operation(
            summary = "최근 검색어 조회",
            description = "사용자의 최근 검색어를 조회합니다. 최대 20개의 검색어를 반환되며, 7일 이내의 검색어만 조회됩니다.")
    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<Object>> getRecentSearches(
            final @AuthenticationPrincipal CustomUserDetails currentUser) {

        SearchKeywordListDTO searchKeywordListDTO =
                searchService.getRecentSearches(currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.from(
                                SearchKeywordListResponse.toSearchKeywordListResponse(searchKeywordListDTO)));
    }

    @Operation(summary = "검색어 삭제", description = "사용자의 최근 검색어를 삭제합니다.")
    @DeleteMapping("/recent")
    public ResponseEntity<ApiResponse<Object>> deleteRecentSearch(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @RequestParam(value = "keyword") String keyword) {

        searchService.removeSearchKeyword(currentUser.getId(), keyword);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }

    @Operation(summary = "검색어 전체 삭제", description = "사용자의 최근 검색어를 전체 삭제합니다.")
    @DeleteMapping("/recent/all")
    public ResponseEntity<ApiResponse<Object>> deleteAllRecentSearch(
            final @AuthenticationPrincipal CustomUserDetails currentUser) {

        searchService.clearRecentSearches(currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }
}
