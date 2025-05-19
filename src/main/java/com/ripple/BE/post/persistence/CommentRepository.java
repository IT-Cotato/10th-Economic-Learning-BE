package com.ripple.BE.post.persistence;

import com.ripple.BE.post.domain.comment.Comment;
import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    void save(final Comment comment);

    void delete(final Comment comment);

    Optional<Comment> findByIdForUpdate(final long commentId);

    void updateReplyCount(final Comment comment);

    void updateLikeCount(final Comment comment);

    // 부모 댓글 ID로 자식 댓글을 조회한다.
    List<Comment> findChildrenByParentId(final long parentId);

    // 게시글 ID로 루트 댓글을 조회한다.
    List<Comment> findRootCommentsByPost(final long postId);

    // 댓글 작성자 ID로 댓글을 조회한다.
    List<Comment> findAllByCommenterId(final long userId);
}
