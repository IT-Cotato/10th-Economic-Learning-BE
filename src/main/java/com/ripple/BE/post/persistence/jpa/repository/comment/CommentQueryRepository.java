package com.ripple.BE.post.persistence.jpa.repository.comment;

import com.ripple.BE.post.persistence.dto.CommentWithUserDTO;
import com.ripple.BE.user.domain.User;
import java.util.List;

public interface CommentQueryRepository {

    List<User> findUsersByToktokPostId(long postId);

    List<CommentWithUserDTO> findAllByPostIdWithUser(long postId);
}
