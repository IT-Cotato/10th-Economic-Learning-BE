package com.ripple.BE.post.persistence.jpa.repository.postlike;

import com.ripple.BE.post.persistence.dto.PostWithImageDTO;
import java.util.List;

public interface PostLikeQueryRepository {

    List<PostWithImageDTO> findPostsLikedByUser(long userId);
}
