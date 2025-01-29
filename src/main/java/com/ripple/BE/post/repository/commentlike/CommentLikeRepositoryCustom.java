package com.ripple.BE.post.repository.commentlike;

import com.ripple.BE.post.domain.Comment;
import java.util.List;

public interface CommentLikeRepositoryCustom {
    List<Comment> findCommentsLikedByUser(Long userId);
}
