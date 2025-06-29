package com.ripple.BE.post.persistence.impl.post;

import com.ripple.BE.post.domain.post.Post;
import com.ripple.BE.post.domain.type.PostSort;
import com.ripple.BE.post.domain.type.PostType;
import com.ripple.BE.post.persistence.PostRepository;
import com.ripple.BE.post.persistence.dto.PostDetailDTO;
import com.ripple.BE.post.persistence.dto.PostWithImageDTO;
import com.ripple.BE.post.persistence.jpa.entity.PostJpaEntity;
import com.ripple.BE.post.persistence.jpa.repository.post.PostJpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
    public Page<PostWithImageDTO> findByType(
            final PostType type, final PostSort postSort, final Pageable pageable) {
        return postJpaRepository.findByType(type, postSort, pageable);
    }

    @Override
    public Page<PostWithImageDTO> findPosts(final Pageable pageable, final PostSort postSort) {
        return postJpaRepository.findNormalPosts(pageable, postSort);
    }

    @Override
    public List<PostWithImageDTO> findPopularPosts() {
        return postJpaRepository.findPopularPosts();
    }

    @Override
    public Page<PostWithImageDTO> searchNormalPosts(final String keyword, final Pageable pageable) {
        return postJpaRepository.searchNormalPosts(keyword, pageable);
    }

    @Override
    public List<PostWithImageDTO> findUserNormalPosts(final long userId) {
        return postJpaRepository.findUserNormalPosts(userId);
    }

    @Override
    public List<PostWithImageDTO> findByIdIn(final List<Long> postIds) {
        return postJpaRepository.findByIdIn(postIds);
    }

    @Override
    public Optional<PostDetailDTO> findPostDetail(final long postId, final long userId) {
        return postJpaRepository.findPostDetail(postId, userId);
    }

    @Override
    public List<Post> findAllByAuthorId(final long authorId) {
        return postJpaRepository.findAllByAuthorId(authorId).stream()
                .map(PostJpaEntity::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAllByAuthorId(final long authorId) {
        postJpaRepository.deleteAllByAuthorId(authorId);
    }
}
