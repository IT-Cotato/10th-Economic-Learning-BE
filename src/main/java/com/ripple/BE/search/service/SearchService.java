package com.ripple.BE.search.service;

import com.ripple.BE.news.dto.response.NewsPreviewListResponseDTO;
import com.ripple.BE.news.persistence.dto.NewsWithScrapDTO;
import com.ripple.BE.news.persistence.jpa.repository.news.NewsJpaRepository;
import com.ripple.BE.search.dto.SearchKeywordListDTO;
import com.ripple.BE.term.domain.Term;
import com.ripple.BE.term.dto.TermListDTO;
import com.ripple.BE.term.repository.TermRepository;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class SearchService {

    private final int PAGE_SIZE = 10;
    private static final int MAX_RECENT_SEARCHES = 20; // 최대 최근 검색어 개수
    private static final long EXPIRATION_TIME = 7; // 데이터 만료 기간 (7일)
    private static final String SEARCH_KEYWORD_KEY = "search:keyword:";

    private final RedisTemplate<String, String> redisTemplate;

    private final NewsJpaRepository newsJpaRepository;
    private final TermRepository termRepository;

    @Cacheable(value = "newsSearch", key = "#keyword != null ? #keyword + #page : #page")
    @Transactional(readOnly = true)
    public NewsPreviewListResponseDTO searchNews(
            final String keyword, final int page, final long userId) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        Page<NewsWithScrapDTO> newsPage = newsJpaRepository.searchNews(keyword, pageable, userId);
        addRecentSearch(userId, keyword);

        return NewsPreviewListResponseDTO.from(newsPage);
    }

    @Cacheable(value = "termSearch", key = "#keyword != null ? #keyword + #page : #page")
    @Transactional(readOnly = true)
    public TermListDTO searchTerms(final String keyword, final int page, final long userId) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        Page<Term> termPage = termRepository.searchTerms(keyword, pageable, userId);
        addRecentSearch(userId, keyword);

        return TermListDTO.toTermListDTO(termPage);
    }

    public SearchKeywordListDTO getRecentSearches(final long userId) {
        String key = getKey(userId);
        List<String> searchKeywords =
                Optional.ofNullable(redisTemplate.opsForList().range(key, 0, -1)).orElse(List.of());

        return SearchKeywordListDTO.toSearchKeywordListDTO(searchKeywords);
    }

    private void addRecentSearch(final long userId, final String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return;
        }

        String key = getKey(userId);

        redisTemplate.opsForList().remove(key, 0, keyword); // 중복 검색어 제거

        redisTemplate.opsForList().leftPush(key, keyword); // 최근 검색어 추가

        redisTemplate.opsForList().trim(key, 0, MAX_RECENT_SEARCHES - 1); // 최근 검색어 개수 제한

        redisTemplate.expire(key, EXPIRATION_TIME, TimeUnit.DAYS); // 만료 시간 설정
    }

    public void clearRecentSearches(final long userId) {
        String key = getKey(userId);
        redisTemplate.delete(key);
    }

    public void removeSearchKeyword(final long userId, String keyword) {
        String key = getKey(userId);

        redisTemplate.opsForList().remove(key, 0, keyword);
    }

    // Redis 키 생성
    private String getKey(final long userId) {
        return SEARCH_KEYWORD_KEY + userId;
    }
}
