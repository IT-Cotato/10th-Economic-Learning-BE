package com.ripple.BE.post.persistence.impl.post;

import com.ripple.BE.post.domain.post.PostLike;
import com.ripple.BE.post.persistence.PostLikeRepository;
import com.ripple.BE.post.persistence.dto.PostWithScrapAndImageDTO;
import com.ripple.BE.post.persistence.jpa.entity.PostLikeJpaEntity;
import com.ripple.BE.post.persistence.jpa.repository.postlike.PostLikeJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostLikeRepositoryImpl implements PostLikeRepository {

    private final PostLikeJpaRepository postLikeJpaRepository;

    @Override
    public boolean existsByPostIdAndUserId(final long postId, final long userId) {
        return postLikeJpaRepository.existsByPostIdAndUserId(postId, userId);
    }

    @Override
    public Optional<PostLike> findByPostIdAndUserId(final long postId, final long userId) {
        return postLikeJpaRepository
                .findByPostIdAndUserId(postId, userId)
                .map(PostLikeJpaEntity::toModel);
    }

    @Override
    public List<PostWithScrapAndImageDTO> findPostsLikedByUser(final long userId) {
        return postLikeJpaRepository.findPostsLikedByUser(userId);
    }

    @Override
    public void delete(final PostLike postLike) {
        postLikeJpaRepository.delete(PostLikeJpaEntity.from(postLike));
    }

    @Override
    public void save(final PostLike postLike) {
        postLikeJpaRepository.save(PostLikeJpaEntity.from(postLike));
    }

    @Override
    public void deleteAllByPostId(final long postId) {
        postLikeJpaRepository.deleteAllByPostId(postId);
    }
}
