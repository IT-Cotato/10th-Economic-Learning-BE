package com.ripple.BE.user.service;

import static com.ripple.BE.post.exception.errorcode.PostErrorCode.*;
import static com.ripple.BE.user.exception.errorcode.UserErrorCode.*;

import com.ripple.BE.image.repository.ImageRepository;
import com.ripple.BE.learning.domain.concept.Concept;
import com.ripple.BE.learning.domain.quiz.Quiz;
import com.ripple.BE.learning.dto.response.quiz.FailQuizResponseDTO;
import com.ripple.BE.learning.dto.response.scrap.ScrapConceptResponseDTO;
import com.ripple.BE.learning.dto.response.scrap.ScrapQuizResponseDTO;
import com.ripple.BE.learning.persistence.ConceptScrapRepository;
import com.ripple.BE.learning.persistence.QuizRepository;
import com.ripple.BE.learning.persistence.QuizScrapRepository;
import com.ripple.BE.news.domain.News;
import com.ripple.BE.news.dto.NewsListDTO;
import com.ripple.BE.news.repository.newscrap.NewsScrapRepository;
import com.ripple.BE.post.domain.comment.Comment;
import com.ripple.BE.post.domain.post.Post;
import com.ripple.BE.post.domain.type.PostType;
import com.ripple.BE.post.dto.response.LikeCommentResponseDTO;
import com.ripple.BE.post.dto.response.PostPreviewResponseDTO;
import com.ripple.BE.post.exception.PostException;
import com.ripple.BE.post.persistence.CommentLikeRepository;
import com.ripple.BE.post.persistence.CommentRepository;
import com.ripple.BE.post.persistence.PostLikeRepository;
import com.ripple.BE.post.persistence.PostRepository;
import com.ripple.BE.post.persistence.PostScrapRepository;
import com.ripple.BE.term.domain.Term;
import com.ripple.BE.term.dto.TermListDTO;
import com.ripple.BE.term.repository.TermScrapRepository;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.domain.type.Level;
import com.ripple.BE.user.dto.UserCommentDTO;
import com.ripple.BE.user.dto.UserCommentListDTO;
import com.ripple.BE.user.dto.UserCompletedDTO;
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

    private final ImageRepository imageRepository;

    public List<PostPreviewResponseDTO> getMyPosts(final long userId) {
        List<Post> posts = postRepository.findUserNormalPosts(userId);

        return posts.stream().map(post -> toPreview(post, userId)).toList();
    }

    public List<PostPreviewResponseDTO> getMyLikePosts(final long userId) {
        List<Post> posts = postLikeRepository.findPostsLikedByUser(userId);
        return posts.stream().map(post -> toPreview(post, userId)).collect(Collectors.toList());
    }

    public List<PostPreviewResponseDTO> getMyToktok(final long userId) {
        List<Comment> comments = commentRepository.findAllByCommenterId(userId);
        List<Long> postIds = getPostIds(comments);

        List<Post> posts =
                postRepository.findByIdIn(postIds).stream()
                        .filter(post -> post.getType() == PostType.ECONOMY_TALK)
                        .toList();

        return posts.stream().map(post -> toPreview(post, userId)).toList();
    }

    public List<PostPreviewResponseDTO> getMyScrapPosts(final long userId) {
        List<Post> posts = postScrapRepository.findPostsScrappedByUser(userId);
        return posts.stream().map(post -> toPreview(post, userId)).toList();
    }

    private PostPreviewResponseDTO toPreview(Post post, long userId) {
        String imageUrl =
                imageRepository.findByPostId(post.getId()).stream()
                        .findFirst()
                        .map(image -> image.getS3Info().getUrl())
                        .orElse(null);

        boolean isScraped = postScrapRepository.existsByPostIdAndUserId(post.getId(), userId);

        return PostPreviewResponseDTO.of(post, imageUrl, isScraped);
    }

    public List<LikeCommentResponseDTO> getMyLikeComments(final long userId) {
        List<Comment> commentsLikedByUser = commentLikeRepository.findCommentsLikedByUser(userId);

        return commentsLikedByUser.stream()
                .map(LikeCommentResponseDTO::from)
                .collect(Collectors.toList());
    }

    public UserCommentListDTO getMyCommentPosts(final long userId) {
        List<Comment> comments = commentRepository.findAllByCommenterId(userId);
        List<Long> postIds = getPostIds(comments);
        List<Post> posts = postRepository.findByIdIn(postIds);

        Map<Long, Post> postMap = posts.stream().collect(Collectors.toMap(Post::getId, post -> post));

        List<UserCommentDTO> userCommentDTOS =
                comments.stream()
                        .map(
                                comment -> {
                                    Post post = postMap.get(comment.getPost().getId());
                                    if (post == null) {
                                        throw new PostException(POST_NOT_FOUND);
                                    }
                                    return UserCommentDTO.of(
                                            comment.getId(),
                                            comment.getContent(),
                                            post.getTitle(),
                                            post.getType(),
                                            comment.getCreatedDate());
                                })
                        .collect(Collectors.toList());

        return new UserCommentListDTO(userCommentDTOS);
    }

    private static List<Long> getPostIds(List<Comment> comments) {
        return comments.stream()
                .map(comment -> comment.getPost().getId())
                .distinct()
                .collect(Collectors.toList());
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

    public TermListDTO getMyScrapTermsByInitial(final long userId, final String initial) {
        List<Term> terms = termScrapRepository.findTermsScrappedByUserAndInitial(userId, initial);
        return TermListDTO.toTermListDTO(terms);
    }

    public TermListDTO getMyScrapTermsByKeyword(final long userId, final String keyword) {
        List<Term> terms = termScrapRepository.findTermsScrappedByUserAndKeyword(userId, keyword);
        return TermListDTO.toTermListDTO(terms);
    }

    public NewsListDTO getMyScrapNews(final long userId) {
        List<News> news = newsScrapRepository.findNewsScrappedByUser(userId);
        return NewsListDTO.toNewsListDTO(news);
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
