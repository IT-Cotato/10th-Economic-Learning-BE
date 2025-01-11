package com.ripple.BE.post.repository.postlike;

import com.ripple.BE.post.domain.Post;
import java.util.List;

public interface PostLikeRepositoryCustom {

    List<Post> findPostsLikedByUser(Long userId);
}
