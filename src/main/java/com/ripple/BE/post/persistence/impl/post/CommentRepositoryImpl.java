package com.ripple.BE.post.persistence.impl.post;

import com.ripple.BE.post.domain.comment.Comment;
import com.ripple.BE.post.persistence.CommentRepository;
import com.ripple.BE.post.persistence.dto.CommentWithPostDTO;
import com.ripple.BE.post.persistence.dto.CommentWithUserDTO;
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
        return commentJpaRepository.findByIdForUpdate(commentId).map(CommentJpaEntity::toModel);
    }

    @Override
    public List<Comment> findAllByCommenterId(final long userId) {
        return commentJpaRepository.findAllByCommenterId(userId).stream()
                .map(CommentJpaEntity::toModel)
                .toList();
    }

    @Override
    public void deleteAllByPostId(final long postId) {
        commentJpaRepository.deleteAllByPostId(postId);
    }

    @Override
    public List<CommentWithUserDTO> findAllByPostIdWithUser(final long postId) {
        return commentJpaRepository.findAllByPostIdWithUser(postId);
    }

    @Override
    public List<CommentWithPostDTO> findUserCommentsWithPost(final long userId) {
        return commentJpaRepository.findUserCommentsWithPost(userId);
    }

    @Override
    public void deleteAllByPostIdIn(final List<Long> postIds) {
        commentJpaRepository.deleteAllByPostIdIn(postIds);
    }

    @Override
    public List<Comment> findAllByPostIdIn(final List<Long> postIds) {
        return commentJpaRepository.findAllByPostIdIn(postIds).stream()
                .map(CommentJpaEntity::toModel)
                .toList();
    }
}
