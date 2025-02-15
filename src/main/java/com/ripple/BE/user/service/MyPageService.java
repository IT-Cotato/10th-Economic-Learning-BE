package com.ripple.BE.user.service;

import static com.ripple.BE.post.exception.errorcode.PostErrorCode.*;
import static com.ripple.BE.user.exception.errorcode.UserErrorCode.*;

import com.ripple.BE.learning.domain.concept.Concept;
import com.ripple.BE.learning.domain.quiz.Quiz;
import com.ripple.BE.learning.dto.ConceptListDTO;
import com.ripple.BE.learning.dto.FailQuizListDTO;
import com.ripple.BE.learning.dto.QuizListDTO;
import com.ripple.BE.learning.repository.conceptScrap.ConceptScrapRepository;
import com.ripple.BE.learning.repository.quiz.QuizRepository;
import com.ripple.BE.learning.repository.quizScrap.QuizScrapRepository;
import com.ripple.BE.news.domain.News;
import com.ripple.BE.news.dto.NewsListDTO;
import com.ripple.BE.news.repository.newscrap.NewsScrapRepository;
import com.ripple.BE.post.domain.Comment;
import com.ripple.BE.post.domain.Post;
import com.ripple.BE.post.dto.LikeCommentListDTO;
import com.ripple.BE.post.dto.PostListDTO;
import com.ripple.BE.post.exception.PostException;
import com.ripple.BE.post.repository.comment.CommentRepository;
import com.ripple.BE.post.repository.commentlike.CommentLikeRepository;
import com.ripple.BE.post.repository.post.PostRepository;
import com.ripple.BE.post.repository.postlike.PostLikeRepository;
import com.ripple.BE.post.repository.postscrap.PostScrapRepository;
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
import java.util.List;
import java.util.Map;
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
    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final PostScrapRepository postScrapRepository;
    private final QuizRepository quizRepository;
    private final QuizScrapRepository quizScrapRepository;
    private final ConceptScrapRepository conceptScrapRepository;
    private final TermScrapRepository termScrapRepository;
    private final NewsScrapRepository newsScrapRepository;
    private final UserRepository userRepository;

    public PostListDTO getMyPosts(final long userId) {

        List<Post> posts = postRepository.findUserNormalPosts(userId);

        return PostListDTO.toPostListDTO(posts);
    }

    public PostListDTO getMyLikePosts(final long userId) {

        List<Post> posts = postLikeRepository.findPostsLikedByUser(userId);

        return PostListDTO.toPostListDTO(posts);
    }

    public UserCommentListDTO getMyCommentPosts(final long userId) {
        List<Comment> comments = commentRepository.findByUserId(userId); // 유저가 단 모든 댓글 조회

        // 댓글이 달린 게시글의 id만 추출
        List<Long> postIds =
                comments.stream()
                        .map(comment -> comment.getPost().getId())
                        .distinct()
                        .collect(Collectors.toList());

        List<Post> posts = postRepository.findByIdIn(postIds); // 게시글 id로 게시글 조회

        Map<Long, Post> postMap =
                posts.stream()
                        .collect(Collectors.toMap(Post::getId, post -> post)); // 게시글 id를 key로 하는 map 생성

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

    public PostListDTO getMyScrapPosts(final long userId) {

        List<Post> posts = postScrapRepository.findPostsScrappedByUser(userId);

        return PostListDTO.toPostListDTO(posts);
    }

    public FailQuizListDTO getMyFailQuizzes(final long userId, Level level) {

        List<Quiz> failedQuizzesByUserAndLevel =
                quizRepository.findFailedQuizzesByUserAndLevel(userId, level);

        return FailQuizListDTO.toFailQuizListDTO(failedQuizzesByUserAndLevel);
    }

    public LikeCommentListDTO getMyLikeComments(final long userId) {

        List<Comment> commentsLikedByUser = commentLikeRepository.findCommentsLikedByUser(userId);

        return LikeCommentListDTO.toLikeCommentListDTO(commentsLikedByUser);
    }

    public QuizListDTO getMyScrapQuizzes(final long userId, final Level level) {

        List<Quiz> quizzes = quizScrapRepository.findQuizScrappedByUserAndLevel(userId, level);

        return QuizListDTO.toQuizScrapListDTO(quizzes);
    }

    public ConceptListDTO getMyConcepts(final long userId, final Level level) {

        List<Concept> concepts =
                conceptScrapRepository.findConceptsScrappedByUserAndLevel(userId, level);

        return ConceptListDTO.toScrapConceptListDTO(concepts);
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

        long beginnerCompletedCount = user.getBeginnerCompletedCount();
        long intermediateCompletedCount = user.getIntermediateCompletedCount();
        long advancedCompletedCount = user.getAdvancedCompletedCount();
        long totalConceptCompletedCount =
                beginnerCompletedCount + intermediateCompletedCount + advancedCompletedCount;

        return UserCompletedDTO.builder()
                .userId(userId)
                .beginnerCompletedCount(beginnerCompletedCount)
                .intermediateCompletedCount(intermediateCompletedCount)
                .advancedCompletedCount(advancedCompletedCount)
                .totalConceptCompletedCount(totalConceptCompletedCount)
                .quizCount(user.getQuizCount())
                .build();
    }
}
