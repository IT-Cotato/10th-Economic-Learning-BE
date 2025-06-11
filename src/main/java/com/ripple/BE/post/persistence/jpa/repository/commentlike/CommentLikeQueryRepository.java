package com.ripple.BE.post.persistence.jpa.repository.commentlike;

import com.ripple.BE.post.persistence.dto.LikeCommentWithPostDTO;
import java.util.List;

public interface CommentLikeQueryRepository {
    List<LikeCommentWithPostDTO> findLikedCommentsByUserIdWithPost(long userId);
}
