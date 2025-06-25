package com.ripple.BE.news.persistence;

import com.ripple.BE.news.domain.NewsScrap;
import com.ripple.BE.news.persistence.dto.NewsWithScrapDTO;
import java.util.List;
import java.util.Optional;

public interface NewsScrapRepository {

    boolean existsByNewsIdAndUserId(long newsId, long userId); // 뉴스 스크랩 여부 확인

    void save(NewsScrap newsScrap);

    Optional<NewsScrap> findByNewsIdAndUserId(long newsId, long userId); // 뉴스 ID와 사용자 ID로 스크랩 정보 조회

    void delete(NewsScrap newsScrap); // 스크랩 삭제

    List<NewsWithScrapDTO> findNewsScrappedByUser(long userId); // 사용자가 스크랩한 뉴스 목록 조회

    void deleteAllByUserId(long userId); // 사용자 ID로 모든 스크랩 삭제
}
