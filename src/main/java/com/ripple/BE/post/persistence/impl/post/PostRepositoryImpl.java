package com.ripple.BE.post.persistence.impl.post;

import com.ripple.BE.post.domain.post.Post;
import com.ripple.BE.post.domain.type.PostSort;
import com.ripple.BE.post.domain.type.PostType;
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
    public Post save(final Post post) {
        PostJpaEntity postJpaEntity = postJpaRepository.save(PostJpaEntity.from(post));
        return postJpaEntity.toModel();
    }

    @Override
    public void delete(final Post post) {
        postJpaRepository.delete(PostJpaEntity.from(post));
    }

    @Override
    public Optional<Post> findById(final long id) {
        return postJpaRepository.findById(id).map(PostJpaEntity::toModel);
    }

    @Override
    public Optional<Post> findByIdForUpdate(final long id) {
        return postJpaRepository.findByIdForUpdate(id).map(PostJpaEntity::toModel);
    }

    @Override
    public Page<Post> findByType(
            final PostType type, final PostSort postSort, final Pageable pageable) {
        return postJpaRepository.findByType(type, postSort, pageable).map(PostJpaEntity::toModel);
    }

    @Override
    public Page<Post> findPosts(final Pageable pageable, final PostSort postSort) {
        return postJpaRepository.findNormalPosts(pageable, postSort).map(PostJpaEntity::toModel);
    }

    @Override
    public List<Post> findPopularPosts() {
        return postJpaRepository.findPopularPosts().stream().map(PostJpaEntity::toModel).toList();
    }

    @Override
    public Page<Post> searchNormalPosts(final String keyword, final Pageable pageable) {
        return postJpaRepository.searchNormalPosts(keyword, pageable).map(PostJpaEntity::toModel);
    }

    @Override
    public List<Post> findUserNormalPosts(final long userId) {
        return postJpaRepository.findUserNormalPosts(userId).stream()
                .map(PostJpaEntity::toModel)
                .toList();
    }

    @Override
    public List<Post> findByIdIn(final List<Long> postIds) {
        return postJpaRepository.findByIdIn(postIds).stream().map(PostJpaEntity::toModel).toList();
    }
}
