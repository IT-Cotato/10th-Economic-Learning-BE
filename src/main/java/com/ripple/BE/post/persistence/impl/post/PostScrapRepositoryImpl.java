package com.ripple.BE.post.persistence.impl.post;

import com.ripple.BE.post.domain.post.Post;
import com.ripple.BE.post.domain.post.PostScrap;
import com.ripple.BE.post.persistence.PostScrapRepository;
import com.ripple.BE.post.persistence.jpa.entity.PostScrapJpaEntity;
import com.ripple.BE.post.persistence.jpa.repository.postscrap.PostScrapJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostScrapRepositoryImpl implements PostScrapRepository {

    private final PostScrapJpaRepository postScrapJpaRepository;

    @Override
    public boolean existsByPostIdAndUserId(final long postId, final long userId) {
        return postScrapJpaRepository.existsByPostIdAndUserId(postId, userId);
    }

    @Override
    public Optional<PostScrap> findByPostIdAndUserId(final long postId, final long userId) {
        return postScrapJpaRepository.findByPostIdAndUserId(postId, userId).map(PostScrap::from);
    }

    @Override
    public void delete(final PostScrap postScrap) {
        postScrapJpaRepository.delete(PostScrapJpaEntity.from(postScrap));
    }

    @Override
    public void save(final PostScrap postScrap) {
        postScrapJpaRepository.save(PostScrapJpaEntity.from(postScrap));
    }

    @Override
    public List<Post> findPostsScrappedByUser(final long userId) {
        return postScrapJpaRepository.findPostsScrappedByUser(userId).stream().map(Post::from).toList();
    }
}
