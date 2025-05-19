package com.ripple.BE.post.persistence.impl.toktok;

import static com.ripple.BE.post.exception.errorcode.PostErrorCode.*;

import com.ripple.BE.post.domain.post.Post;
import com.ripple.BE.post.domain.type.PostSort;
import com.ripple.BE.post.exception.PostException;
import com.ripple.BE.post.persistence.ToktokRepository;
import com.ripple.BE.post.persistence.jpa.entity.PostJpaEntity;
import com.ripple.BE.post.persistence.jpa.repository.comment.CommentJpaRepository;
import com.ripple.BE.post.persistence.jpa.repository.post.PostJpaRepository;
import com.ripple.BE.user.domain.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ToktokRepositoryImpl implements ToktokRepository {

    private final CommentJpaRepository commentJpaRepository;
    private final PostJpaRepository toktokJpaRepository;

    @Override
    public Post save(final Post post) {
        PostJpaEntity postJpaEntity = toktokJpaRepository.save(PostJpaEntity.from(post));
        return Post.from(postJpaEntity);
    }

    @Override
    public void delete(final Post post) {
        PostJpaEntity postJpaEntity = PostJpaEntity.from(post);
        toktokJpaRepository.delete(postJpaEntity);
    }

    // 게시글에 달린 댓글을 작성한 유저를 찾는 쿼리
    @Override
    public List<User> findUsersByToktokPostId(final long postId) {
        return commentJpaRepository.findUsersByToktokPostId(postId);
    }

    @Override
    public Optional<Post> findByUsedDate(final LocalDate usedDate) {
        return toktokJpaRepository.findByUsedDate(usedDate).map(Post::from);
    }

    @Override
    public Optional<Post> findById(final long id) {
        return toktokJpaRepository.findById(id).map(Post::from);
    }

    @Override
    public Page<Post> findUsedToktokPosts(final Pageable pageable, final PostSort postSort) {
        return toktokJpaRepository.findUsedToktokPosts(pageable, postSort).map(Post::from);
    }

    @Override
    public List<Post> findNewToktokPosts() {
        return toktokJpaRepository.findNewToktokPosts().stream().map(Post::from).toList();
    }

    @Override
    public Set<String> findAllTitles() {
        return toktokJpaRepository.findAllTitles();
    }

    @Override
    public Page<Post> searchUsedToktokPosts(final String keyword, final Pageable pageable) {
        return toktokJpaRepository.searchUsedToktokPosts(keyword, pageable).map(Post::from);
    }

    @Override
    public void updateUsedDate(final Post post) {
        PostJpaEntity postJpaEntity =
                toktokJpaRepository
                        .findById(post.getId())
                        .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        postJpaEntity.updateUsedDate(post.getUsedDate());
    }
}
