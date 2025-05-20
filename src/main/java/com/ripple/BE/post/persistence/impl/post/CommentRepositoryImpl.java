package com.ripple.BE.post.persistence.impl.post;

import static com.ripple.BE.post.exception.errorcode.PostErrorCode.*;

import com.ripple.BE.post.domain.comment.Comment;
import com.ripple.BE.post.exception.PostException;
import com.ripple.BE.post.persistence.CommentRepository;
import com.ripple.BE.post.persistence.jpa.entity.CommentJpaEntity;
import com.ripple.BE.post.persistence.jpa.repository.comment.CommentJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

    private final CommentJpaRepository commentJpaRepository;

    @Override
    public void save(final Comment comment) {
        commentJpaRepository.save(CommentJpaEntity.from(comment));
    }

    @Override
    public void delete(final Comment comment) {
        commentJpaRepository.delete(CommentJpaEntity.from(comment));
    }

    @Override
    public Optional<Comment> findByIdForUpdate(final long commentId) {
        return commentJpaRepository.findByIdForUpdate(commentId).map(Comment::from);
    }

    @Override
    public void updateReplyCount(final Comment comment) {
        CommentJpaEntity commentJpaEntity =
                commentJpaRepository
                        .findById(comment.getId())
                        .orElseThrow(() -> new PostException(COMMENT_NOT_FOUND));

        commentJpaEntity.updateReplyCount(comment.getReplyCount());
    }

    @Override
    public void updateLikeCount(final Comment comment) {
        CommentJpaEntity commentJpaEntity =
                commentJpaRepository
                        .findById(comment.getId())
                        .orElseThrow(() -> new PostException(COMMENT_NOT_FOUND));

        commentJpaEntity.updateLikeCount(comment.getLikeCount());
    }

    @Override
    public List<Comment> findRootCommentsByPost(final long postId) {

        return commentJpaRepository.findRootCommentsByPost(postId).stream().map(Comment::from).toList();
    }

    @Override
    public List<Comment> findChildrenByParentId(final long parentId) {
        return commentJpaRepository.findChildrenByParentId(parentId).stream()
                .map(Comment::from)
                .toList();
    }

    @Override
    public List<Comment> findAllByCommenterId(final long userId) {
        return commentJpaRepository.findAllByCommenterId(userId).stream().map(Comment::from).toList();
    }

    @Override
    public void deleteAllByPostId(final long postId) {
        commentJpaRepository.deleteAllByPostId(postId);
    }
}
