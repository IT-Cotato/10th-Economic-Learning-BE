package com.ripple.BE.post.persistence.impl.post;

import static com.ripple.BE.post.exception.errorcode.PostErrorCode.*;

import com.ripple.BE.post.domain.post.Post;
import com.ripple.BE.post.domain.type.PostSort;
import com.ripple.BE.post.domain.type.PostType;
import com.ripple.BE.post.exception.PostException;
import com.ripple.BE.post.persistence.PostRepository;
import com.ripple.BE.post.persistence.jpa.entity.PostJpaEntity;
import com.ripple.BE.post.persistence.jpa.repository.post.PostJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

    private final PostJpaRepository postJpaRepository;

    @Override
    public void save(final Post post) {
        postJpaRepository.save(PostJpaEntity.from(post));
    }

    @Override
    public void delete(final Post post) {
        postJpaRepository.delete(PostJpaEntity.from(post));
    }

    @Override
    public void update(final Post post) {
        PostJpaEntity postJpaEntity =
                postJpaRepository
                        .findById(post.getId())
                        .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        postJpaEntity.update(post);
    }

    @Override
    public void updateCommentCount(final Post post) {
        PostJpaEntity postJpaEntity =
                postJpaRepository
                        .findById(post.getId())
                        .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        postJpaEntity.updateCommentCount(post.getCommentCount());
    }

    @Override
    public void updateLikeCount(final Post post) {
        PostJpaEntity postJpaEntity =
                postJpaRepository
                        .findById(post.getId())
                        .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        postJpaEntity.updateLikeCount(post.getLikeCount());
    }

    @Override
    public void updateScrapCount(final Post post) {
        PostJpaEntity postJpaEntity =
                postJpaRepository
                        .findById(post.getId())
                        .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        postJpaEntity.updateScrapCount(post.getScrapCount());
    }

    @Override
    public Optional<Post> findById(final long id) {
        return postJpaRepository.findById(id).map(Post::from);
    }

    @Override
    public Optional<Post> findByIdForUpdate(final long id) {
        return postJpaRepository.findByIdForUpdate(id).map(Post::from);
    }

    @Override
    public Page<Post> findByType(
            final PostType type, final PostSort postSort, final Pageable pageable) {
        return postJpaRepository.findByType(type, postSort, pageable).map(Post::from);
    }

    @Override
    public Page<Post> findPosts(final Pageable pageable, final PostSort postSort) {
        return postJpaRepository.findNormalPosts(pageable, postSort).map(Post::from);
    }

    @Override
    public List<Post> findPopularPosts() {
        return postJpaRepository.findPopularPosts().stream().map(Post::from).toList();
    }

    @Override
    public Page<Post> searchNormalPosts(final String keyword, final Pageable pageable) {
        return postJpaRepository.searchNormalPosts(keyword, pageable).map(Post::from);
    }

    @Override
    public List<Post> findUserNormalPosts(final long userId) {
        return postJpaRepository.findUserNormalPosts(userId).stream().map(Post::from).toList();
    }

    @Override
    public List<Post> findByIdIn(final List<Long> postIds) {
        return postJpaRepository.findByIdIn(postIds).stream().map(Post::from).toList();
    }
}
