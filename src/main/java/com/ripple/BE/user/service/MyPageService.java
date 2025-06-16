package com.ripple.BE.user.service;

import static com.ripple.BE.user.exception.errorcode.UserErrorCode.*;

import com.ripple.BE.learning.domain.concept.Concept;
import com.ripple.BE.learning.domain.quiz.Quiz;
import com.ripple.BE.learning.dto.response.quiz.FailQuizResponseDTO;
import com.ripple.BE.learning.dto.response.scrap.ScrapConceptResponseDTO;
import com.ripple.BE.learning.dto.response.scrap.ScrapQuizResponseDTO;
import com.ripple.BE.learning.persistence.ConceptScrapRepository;
import com.ripple.BE.learning.persistence.QuizRepository;
import com.ripple.BE.learning.persistence.QuizScrapRepository;
import com.ripple.BE.news.dto.response.NewsPreviewListResponseDTO;
import com.ripple.BE.news.persistence.NewsScrapRepository;
import com.ripple.BE.news.persistence.dto.NewsWithScrapDTO;
import com.ripple.BE.post.domain.comment.Comment;
import com.ripple.BE.post.dto.response.LikeCommentResponseDTO;
import com.ripple.BE.post.dto.response.PostPreviewResponseDTO;
import com.ripple.BE.post.persistence.CommentLikeRepository;
import com.ripple.BE.post.persistence.CommentRepository;
import com.ripple.BE.post.persistence.PostLikeRepository;
import com.ripple.BE.post.persistence.PostRepository;
import com.ripple.BE.post.persistence.PostScrapRepository;
import com.ripple.BE.post.persistence.dto.CommentWithPostDTO;
import com.ripple.BE.post.persistence.dto.PostWithScrapAndImageDTO;
import com.ripple.BE.term.dto.response.TermListResponseDTO;
import com.ripple.BE.term.persistence.TermScrapRepository;
import com.ripple.BE.term.persistence.dto.TermWithScrapDTO;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.domain.type.Level;
import com.ripple.BE.user.dto.UserCompletedDTO;
import com.ripple.BE.user.dto.response.UserCommentResponseDTO;
import com.ripple.BE.user.exception.UserException;
import com.ripple.BE.user.repository.UserRepository;
import java.util.*;
import java.util.stream.Collectors;
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
    private final PostScrapRepository postScrapRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    private final TermScrapRepository termScrapRepository;
    private final NewsScrapRepository newsScrapRepository;
    private final UserRepository userRepository;

    private final QuizRepository quizRepository;
    private final QuizScrapRepository quizScrapRepository;
    private final ConceptScrapRepository conceptScrapRepository;

    public List<PostPreviewResponseDTO> getMyPosts(final long userId) {
        List<PostWithScrapAndImageDTO> posts = postRepository.findUserNormalPosts(userId);

        return posts.stream().map(PostPreviewResponseDTO::from).collect(Collectors.toList());
    }

    public List<PostPreviewResponseDTO> getMyLikePosts(final long userId) {
        List<PostWithScrapAndImageDTO> posts = postLikeRepository.findPostsLikedByUser(userId);
        return posts.stream().map(PostPreviewResponseDTO::from).collect(Collectors.toList());
    }

    public List<PostPreviewResponseDTO> getMyToktok(final long userId) {
        List<Comment> comments = commentRepository.findAllByCommenterId(userId);
        List<Long> postIds = getPostIds(comments);

        List<PostWithScrapAndImageDTO> posts = postRepository.findByIdIn(postIds, userId);

        return posts.stream().map(PostPreviewResponseDTO::from).toList();
    }

    public List<PostPreviewResponseDTO> getMyScrapPosts(final long userId) {
        List<PostWithScrapAndImageDTO> posts = postScrapRepository.findPostsScrappedByUser(userId);
        return posts.stream().map(PostPreviewResponseDTO::from).collect(Collectors.toList());
    }

    public List<LikeCommentResponseDTO> getMyLikeComments(final long userId) {
        return commentLikeRepository.findLikedCommentsByUserIdWithPost(userId).stream()
                .map(LikeCommentResponseDTO::from)
                .toList();
    }

    public List<UserCommentResponseDTO> getMyCommentPosts(final long userId) {
        List<CommentWithPostDTO> comments = commentRepository.findUserCommentsWithPost(userId);
        return comments.stream().map(UserCommentResponseDTO::from).collect(Collectors.toList());
    }

    private static List<Long> getPostIds(List<Comment> comments) {
        return comments.stream().map(Comment::getPostId).distinct().collect(Collectors.toList());
    }

    public List<FailQuizResponseDTO> getMyFailQuizzes(final long userId, Level level) {
        List<Quiz> failedQuizzes = quizRepository.findFailedQuizzesByUserAndLevel(userId, level);

        return failedQuizzes.stream().map(FailQuizResponseDTO::from).collect(Collectors.toList());
    }

    /** 사용자가 스크랩한 퀴즈를 레벨별로 조회 */
    public List<ScrapQuizResponseDTO> getMyScrapQuizzes(final long userId, final Level level) {
        List<Quiz> quizList = quizScrapRepository.findQuizScrappedByUserIdAndLevel(userId, level);

        return quizList.stream().map(ScrapQuizResponseDTO::from).collect(Collectors.toList());
    }

    /** 사용자가 스크랩한 개념을 레벨별로 조회 */
    public List<ScrapConceptResponseDTO> getMyConcepts(final long userId, final Level level) {
        List<Concept> conceptList =
                conceptScrapRepository.findConceptsScrappedByUserAndLevel(userId, level);

        return conceptList.stream().map(ScrapConceptResponseDTO::from).collect(Collectors.toList());
    }

    public TermListResponseDTO getMyScrapTermsByInitial(final long userId, final String initial) {
        List<TermWithScrapDTO> terms =
                termScrapRepository.findTermsScrappedByUserAndInitial(userId, initial);
        return TermListResponseDTO.from(terms);
    }

    public TermListResponseDTO getMyScrapTermsByKeyword(final long userId, final String keyword) {
        List<TermWithScrapDTO> terms =
                termScrapRepository.findTermsScrappedByUserAndKeyword(userId, keyword);
        return TermListResponseDTO.from(terms);
    }

    public NewsPreviewListResponseDTO getMyScrapNews(final long userId) {
        List<NewsWithScrapDTO> newsList = newsScrapRepository.findNewsScrappedByUser(userId);
        return NewsPreviewListResponseDTO.from(newsList);
    }

    public UserCompletedDTO getCompletedConceptAndQuizCount(final long userId) {
        User user =
                userRepository.findById(userId).orElseThrow(() -> new UserException(USER_NOT_FOUND));

        long beginner = user.getBeginnerCompletedCount();
        long intermediate = user.getIntermediateCompletedCount();
        long advanced = user.getAdvancedCompletedCount();
        long total = beginner + intermediate + advanced;

        return UserCompletedDTO.builder()
                .userId(userId)
                .beginnerCompletedCount(beginner)
                .intermediateCompletedCount(intermediate)
                .advancedCompletedCount(advanced)
                .totalConceptCompletedCount(total)
                .quizCount(user.getQuizCount())
                .build();
    }
}
