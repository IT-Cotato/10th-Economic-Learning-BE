package com.ripple.BE.post.persistence;

import com.ripple.BE.post.domain.post.Post;
import com.ripple.BE.post.domain.post.PostLike;
import java.util.List;
import java.util.Optional;

public interface PostLikeRepository {

    boolean existsByPostIdAndUserId(final long postId, final long userId);

    Optional<PostLike> findByPostIdAndUserId(final long postId, final long userId);

    // 사용자가 좋아요한 게시물 목록을 가져온다.
    List<Post> findPostsLikedByUser(final long userId);

    void delete(final PostLike postLike);

    void save(final PostLike postLike);

    void deleteAllByPostId(final long postId);
}
