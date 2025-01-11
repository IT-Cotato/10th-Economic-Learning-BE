package com.ripple.BE.post.repository.postscrap;

import com.ripple.BE.post.domain.Post;
import java.util.List;

public interface PostScrapRepositoryCustom {

    List<Post> findPostsScrappedByUser(Long userId);
}
