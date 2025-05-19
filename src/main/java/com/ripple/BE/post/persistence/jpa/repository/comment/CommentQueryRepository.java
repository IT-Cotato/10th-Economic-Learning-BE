package com.ripple.BE.post.persistence.jpa.repository.comment;

import com.ripple.BE.post.persistence.jpa.entity.CommentJpaEntity;
import com.ripple.BE.user.domain.User;
import java.util.List;

public interface CommentQueryRepository {

    List<CommentJpaEntity> findRootCommentsByPost(long postId);

    List<User> findUsersByToktokPostId(long postId);
}
