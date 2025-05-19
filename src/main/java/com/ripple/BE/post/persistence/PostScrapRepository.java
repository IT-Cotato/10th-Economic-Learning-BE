package com.ripple.BE.post.persistence;

import com.ripple.BE.post.domain.post.Post;
import com.ripple.BE.post.domain.post.PostScrap;
import java.util.List;
import java.util.Optional;

public interface PostScrapRepository {

    boolean existsByPostIdAndUserId(final long postId, final long userId);

    Optional<PostScrap> findByPostIdAndUserId(final long postId, final long userId);

    void delete(final PostScrap postScrap);

    void save(final PostScrap postScrap);

    // 사용자가 스크랩한 게시물 목록을 조회하낟.
    List<Post> findPostsScrappedByUser(final long userId);
}
