package com.ripple.BE.post.persistence.jpa.repository.postlike;

import com.ripple.BE.post.persistence.dto.PostWithScrapAndImageDTO;
import java.util.List;

public interface PostLikeQueryRepository {

    List<PostWithScrapAndImageDTO> findPostsLikedByUser(long userId);
}
