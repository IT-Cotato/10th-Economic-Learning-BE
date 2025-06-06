package com.ripple.BE.post.persistence;

import com.ripple.BE.post.domain.post.Post;
import com.ripple.BE.post.domain.type.PostSort;
import com.ripple.BE.post.domain.type.PostType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepository {

    Post save(final Post post);

    void delete(final Post post);

    Optional<Post> findById(final long id);

    Optional<Post> findByIdForUpdate(final long id);

    // 게시글 종류를 통해 게시글을 조회한다.
    Page<Post> findByType(final PostType type, final PostSort postSort, final Pageable pageable);

    // 모든 게시글을 조회한다.
    Page<Post> findPosts(final Pageable pageable, final PostSort postSort);

    // 인기 게시글을 조회한다.
    List<Post> findPopularPosts();

    // 사용자가 작성한 게시글을 조회한다.
    List<Post> findUserNormalPosts(final long userId);

    // 게시물을 검색한다.
    Page<Post> searchNormalPosts(final String keyword, final Pageable pageable);

    // 게시글 ID 목록을 통해 게시글을 조회한다.
    List<Post> findByIdIn(final List<Long> postIds);
}
