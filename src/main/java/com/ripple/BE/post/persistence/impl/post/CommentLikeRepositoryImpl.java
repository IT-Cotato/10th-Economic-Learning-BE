package com.ripple.BE.post.persistence.impl.post;

import com.ripple.BE.post.domain.comment.CommentLike;
import com.ripple.BE.post.persistence.CommentLikeRepository;
import com.ripple.BE.post.persistence.dto.LikeCommentWithPostDTO;
import com.ripple.BE.post.persistence.jpa.entity.CommentLikeJpaEntity;
import com.ripple.BE.post.persistence.jpa.repository.commentlike.CommentLikeJpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommentLikeRepositoryImpl implements CommentLikeRepository {

    private final CommentLikeJpaRepository commentLikeJpaRepository;

    @Override
    public Optional<CommentLike> findByCommentIdAndUserId(final long commentId, final long userId) {
        return commentLikeJpaRepository
                .findByCommentIdAndUserId(commentId, userId)
                .map(CommentLikeJpaEntity::toModel);
    }

    @Override
    public boolean existsByCommentIdAndUserId(final long commentId, final long userId) {
        return commentLikeJpaRepository.existsByCommentIdAndUserId(commentId, userId);
    }

    @Override
    public void save(final CommentLike commentLike) {
        commentLikeJpaRepository.save(CommentLikeJpaEntity.from(commentLike));
    }

    @Override
    public void delete(final CommentLike commentLike) {
        commentLikeJpaRepository.delete(CommentLikeJpaEntity.from(commentLike));
    }

    @Override
    public void deleteAllByCommentId(final long commentId) {
        commentLikeJpaRepository.deleteAllByCommentId(commentId);
    }

    @Override
    public void deleteAllByPostId(final long postId) {
        commentLikeJpaRepository.deleteAllByPostId(postId);
    }

    @Override
    public List<CommentLike> findByUserIdAndCommentIdIn(
            final long userId, final Set<Long> commentIds) {
        return commentLikeJpaRepository.findByUserIdAndCommentIdIn(userId, commentIds).stream()
                .map(CommentLikeJpaEntity::toModel)
                .toList();
    }

    @Override
    public List<LikeCommentWithPostDTO> findLikedCommentsByUserIdWithPost(long userId) {
        return commentLikeJpaRepository.findLikedCommentsByUserIdWithPost(userId);
    }

    @Override
    public void deleteAllByPostIdIn(final List<Long> postIds) {
        commentLikeJpaRepository.deleteAllByPostIdIn(postIds);
    }
}
