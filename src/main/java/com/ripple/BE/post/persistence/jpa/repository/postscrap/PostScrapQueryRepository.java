package com.ripple.BE.post.persistence.jpa.repository.postscrap;

import com.ripple.BE.post.persistence.dto.PostWithImageDTO;
import java.util.List;

public interface PostScrapQueryRepository {

    List<PostWithImageDTO> findPostsScrappedByUser(long userId);
}
