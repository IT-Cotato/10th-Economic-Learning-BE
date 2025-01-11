package com.ripple.BE.post.service;

import static com.ripple.BE.post.domain.type.PostType.*;
import static com.ripple.BE.post.exception.errorcode.PostErrorCode.*;

import com.ripple.BE.post.domain.Comment;
import com.ripple.BE.post.domain.Post;
import com.ripple.BE.post.domain.type.PostSort;
import com.ripple.BE.post.dto.CommentListDTO;
import com.ripple.BE.post.dto.PostDTO;
import com.ripple.BE.post.dto.PostListDTO;
import com.ripple.BE.post.exception.PostException;
import com.ripple.BE.post.repository.comment.CommentRepository;
import com.ripple.BE.post.repository.post.PostRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class ToktokService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    private static final int PAGE_SIZE = 10;

    private final PostService postService;

    // 오늘의 경제톡톡 주제 미리보기
    @Transactional(readOnly = true)
    public PostDTO getTodayToktok() {

        Post toktok = findTodayToktok();

        return PostDTO.toPostDTO(toktok);
    }

    @Transactional(readOnly = true)
    public PostDTO getToktok(final long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostException(POST_NOT_FOUND));
        if (post.getUsedDate() == null) {
            throw new PostException(POST_NOT_FOUND);
        }

        CommentListDTO commentListDTO = getCommentList(post);

        return PostDTO.toPostDTO(post, commentListDTO);
    }

    @Transactional(readOnly = true)
    public PostListDTO getToktoks(final int page, final PostSort sort) {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        // 게시글 조회 (타입에 따른 필터링)
        Page<Post> postPage = postRepository.findUsedToktokPosts(pageable, sort);

        return PostListDTO.toPostListDTO(postPage);
    }

    private CommentListDTO getCommentList(final Post post) {
        List<Comment> commentList = commentRepository.findRootCommentsByPost(post);

        return CommentListDTO.toCommentListDTO(commentList);
    }

    private Post findTodayToktok() {
        return postRepository
                .findTodayToktokPost(LocalDate.now())
                .orElseThrow(() -> new PostException(TOKTOK_POST_NOT_FOUND));
    }

    // 오늘의 게시물을 저장하는 메서드
    @Transactional
    @Scheduled(cron = "0 0 0 * * *") // 매일 00시 00분 00초에 실행
    public void updateTodayToktok() {

        Random random = new Random();

        // 오늘 이전에 선정된 게시글을 제외하고 게시글 목록 조회
        List<Post> unusedPosts = postRepository.findNewToktokPosts();

        if (unusedPosts.isEmpty()) {
            throw new PostException(TOKTOK_POST_NOT_FOUND);
        }

        Post selectedPost = unusedPosts.get(random.nextInt(unusedPosts.size()));
        selectedPost.setUsedDate(LocalDate.now());
    }
}
