package com.ripple.BE.user.service;

import com.ripple.BE.post.domain.Post;
import com.ripple.BE.post.dto.PostListDTO;
import com.ripple.BE.post.repository.comment.CommentRepository;
import com.ripple.BE.post.repository.post.PostRepository;
import com.ripple.BE.post.repository.postlike.PostLikeRepository;
import com.ripple.BE.post.repository.postscrap.PostScrapRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MyPageService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final PostScrapRepository postScrapRepository;

    @Transactional(readOnly = true)
    public PostListDTO getMyPosts(final long userId) {

        List<Post> posts = postRepository.findUserNormalPosts(userId);

        return PostListDTO.toPostListDTO(posts);
    }

    @Transactional(readOnly = true)
    public PostListDTO getMyLikePosts(final long userId) {

        List<Post> posts = postLikeRepository.findPostsLikedByUser(userId);

        return PostListDTO.toPostListDTO(posts);
    }

    @Transactional(readOnly = true)
    public PostListDTO getMyCommentPosts(final long userId) {

        List<Post> posts = commentRepository.findPostsCommentedByUser(userId);

        return PostListDTO.toPostListDTO(posts);
    }

    @Transactional(readOnly = true)
    public PostListDTO getMyScrapPosts(final long userId) {

        List<Post> posts = postScrapRepository.findPostsScrappedByUser(userId);

        return PostListDTO.toPostListDTO(posts);
    }
}
