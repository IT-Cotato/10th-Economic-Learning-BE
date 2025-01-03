package com.ripple.BE.post.service;

import static com.ripple.BE.post.domain.type.PostType.*;
import static com.ripple.BE.post.exception.errorcode.PostErrorCode.*;

import com.ripple.BE.post.domain.Comment;
import com.ripple.BE.post.domain.Post;
import com.ripple.BE.post.domain.TodayToktok;
import com.ripple.BE.post.dto.CommentListDTO;
import com.ripple.BE.post.dto.PostDTO;
import com.ripple.BE.post.exception.PostException;
import com.ripple.BE.post.repository.PostRepository;
import com.ripple.BE.post.repository.TodayToktokRepository;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class ToktokService {

    private final TodayToktokRepository todayToktokRepository;
    private final PostRepository postRepository;

    private final PostService postService;

    // 오늘의 게시물을 조회하는 메서드
    @Transactional(readOnly = true)
    public PostDTO getToktok() {

        TodayToktok todayToktok = findTodayToktokByDate(LocalDate.now());

        Post post = todayToktok.getPost();

        CommentListDTO commentListDTO = getCommentList(post);

        return PostDTO.toPostDTO(post, commentListDTO);
    }

    @Transactional(readOnly = true)
    public CommentListDTO getCommentList(final Post post) {
        List<Comment> commentList =
                post.getCommentList().stream()
                        .filter(comment -> comment.getParent() == null) // 대댓글이 아닌 경우 필터링
                        .sorted(Comparator.comparing(Comment::getLikeCount).reversed()) // 시간순 내림차순 정렬
                        .toList();

        return CommentListDTO.toCommentListDTO(commentList);
    }

    private TodayToktok findTodayToktokByDate(LocalDate date) {
        return todayToktokRepository
                .findBySelectedDate(date)
                .orElseThrow(() -> new PostException(TODAY_TOKTOK_NOT_FOUND));
    }

    // 오늘의 게시물을 저장하는 메서드
    @Transactional
    @Scheduled(cron = "0 0 0 * * *") // 매일 00시 00분 00초에 실행
    public void updateTodayToktok() {

        Random random = new Random();

        // 오늘 이전에 선정된 게시글을 제외하고 게시글 목록 조회
        List<Post> unselectedPosts = postRepository.findUnselectedPosts(ECONOMY_TALK);

        if (unselectedPosts.isEmpty()) {
            throw new PostException(TOKTOK_POST_NOT_FOUND);
        }

        Post selectedPost = unselectedPosts.get(random.nextInt(unselectedPosts.size()));
        todayToktokRepository.save(TodayToktok.toTodayToktok(selectedPost, LocalDate.now()));
    }
}
