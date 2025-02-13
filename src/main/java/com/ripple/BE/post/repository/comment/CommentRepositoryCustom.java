package com.ripple.BE.post.repository.comment;

import com.ripple.BE.post.domain.Comment;
import com.ripple.BE.post.domain.Post;
import com.ripple.BE.user.domain.User;
import java.util.List;

public interface CommentRepositoryCustom {

    List<Post> findPostsCommentedByUser(Long userId);

    List<Comment> findRootCommentsByPost(Post post, Long userId);

    List<User> findUsersByPostId(Long postId);
}
