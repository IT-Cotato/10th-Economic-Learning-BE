package com.ripple.BE.post.persistence.jpa.repository.comment;

import com.ripple.BE.post.persistence.dto.CommentWithPostDTO;
import com.ripple.BE.post.persistence.dto.CommentWithUserDTO;
import com.ripple.BE.user.domain.User;
import java.util.List;
import java.util.Map;

public interface CommentQueryRepository {

    List<User> findUsersByToktokPostId(long postId);

    Map<Long, List<User>> findUsersByToktokPostIds(List<Long> postIds);

    List<CommentWithUserDTO> findAllByPostIdWithUser(long postId);

    List<CommentWithPostDTO> findUserCommentsWithPost(long userId);
}
