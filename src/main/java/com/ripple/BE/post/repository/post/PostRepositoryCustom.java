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

    Page<Post> findByType(PostType type, PostSort postSort, Pageable pageable);

    Page<Post> findNormalPosts(Pageable pageable, PostSort postSort);

    List<Post> findUserNormalPosts(Long userId);

    List<Post> findNewToktokPosts();

    Page<Post> findUsedToktokPosts(Pageable pageable, PostSort postSort);

    Optional<Post> findTodayToktokPost(LocalDate today);
}
