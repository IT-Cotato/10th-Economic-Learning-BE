package com.ripple.BE.post.persistence.jpa.repository.postscrap;

import com.ripple.BE.post.persistence.dto.PostWithScrapAndImageDTO;
import java.util.List;

public interface PostScrapQueryRepository {

    List<PostWithScrapAndImageDTO> findPostsScrappedByUser(long userId);
}
