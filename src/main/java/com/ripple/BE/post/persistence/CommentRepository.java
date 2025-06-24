package com.ripple.BE.post.persistence;

import com.ripple.BE.post.domain.comment.Comment;
import com.ripple.BE.post.persistence.dto.CommentWithPostDTO;
import com.ripple.BE.post.persistence.dto.CommentWithUserDTO;
import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    void save(final Comment comment);

    void delete(final Comment comment);

    Optional<Comment> findByIdForUpdate(final long commentId);

    List<CommentWithUserDTO> findAllByPostIdWithUser(final long postId);

    // 댓글 작성자 ID로 댓글을 조회한다.
    List<Comment> findAllByCommenterId(final long userId);

    List<CommentWithPostDTO> findUserCommentsWithPost(final long userId);

    void deleteAllByPostId(final long postId);

    void deleteAllByPostIdIn(final List<Long> postIds);

    List<Comment> findAllByPostIdIn(final List<Long> postIds);
}
