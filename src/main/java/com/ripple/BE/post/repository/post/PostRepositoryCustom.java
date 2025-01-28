package com.ripple.BE.post.repository.post;

import com.ripple.BE.post.domain.Post;
import com.ripple.BE.post.domain.type.PostSort;
import com.ripple.BE.post.domain.type.PostType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    Page<Post> findByType(PostType type, PostSort postSort, Pageable pageable, long userId);

    Page<Post> findNormalPosts(Pageable pageable, PostSort postSort, long userId);

    List<Post> findUserNormalPosts(Long userId);

    List<Post> findNewToktokPosts();

    Page<Post> findUsedToktokPosts(Pageable pageable, PostSort postSort, long userId);

    Optional<Post> findTodayToktokPost(LocalDate today);

    Page<Post> searchNormalPosts(String keyword, Pageable pageable, long userId);

    Page<Post> searchUsedToktokPosts(String keyword, Pageable pageable, long userId);
}
