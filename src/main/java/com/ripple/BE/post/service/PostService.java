package com.ripple.BE.post.service;

import static com.ripple.BE.post.exception.errorcode.PostErrorCode.*;

import com.ripple.BE.post.domain.Comment;
import com.ripple.BE.post.domain.CommentLike;
import com.ripple.BE.post.domain.Post;
import com.ripple.BE.post.domain.PostLike;
import com.ripple.BE.post.domain.PostScrap;
import com.ripple.BE.post.domain.type.PostSort;
import com.ripple.BE.post.domain.type.PostType;
import com.ripple.BE.post.dto.CommentListDTO;
import com.ripple.BE.post.dto.PostDTO;
import com.ripple.BE.post.dto.PostListDTO;
import com.ripple.BE.post.exception.PostException;
import com.ripple.BE.post.repository.CommentLikeRepository;
import com.ripple.BE.post.repository.CommentRepository;
import com.ripple.BE.post.repository.PostLikeRepository;
import com.ripple.BE.post.repository.PostRepository;
import com.ripple.BE.post.repository.PostScrapRepository;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.service.UserService;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostScrapRepository postScrapRepository;
    private final CommentLikeRepository commentLikeRepository;

    private final UserService userService;

    private static final int PAGE_SIZE = 10;
    private static final String LIKE_COUNT = "likeCount";
    private static final String CREATED_DATE = "createdDate";

    @Transactional
    public void createPost(
            final long userId, final PostDTO postDTO, final List<MultipartFile> imageList) {
        User user = userService.findUserById(userId);

        Post post = Post.toPostEntity(postDTO);
        post.setAuthor(user);

        /** 이미지 S3 업로드 로직 추후 추가 */
        postRepository.save(post);
    }

    @Transactional
    public void deletePost(final long postId, final long userId) {
        Post post = findPostByIdForUpdate(postId);

        if (post.getAuthor().getId() != userId) {
            throw new PostException(POST_NOT_AUTHORIZED);
        }

        postRepository.delete(post);
    }

    @Transactional(readOnly = true)
    public PostListDTO getPosts(final int page, final PostSort sort, final PostType type) {

        Sort sortOption =
                sort == PostSort.POPULAR
                        ? Sort.by(Sort.Direction.DESC, LIKE_COUNT, CREATED_DATE) // 인기순: 좋아요 수 내림차순, 생성일 내림차순
                        : Sort.by(Sort.Direction.DESC, CREATED_DATE); // 최신순: 생성일 내림차순

        Pageable pageable = PageRequest.of(page, PAGE_SIZE, sortOption);

        // 게시글 조회 (타입에 따른 필터링)
        Page<Post> postPage =
                type == null
                        ? postRepository.findAll(pageable) // 타입 없으면 전체 조회
                        : postRepository.findByType(type, pageable); // 타입에 따른 조회

        return PostListDTO.toPostListDTO(postPage);
    }

    @Transactional(readOnly = true)
    public PostDTO getPost(final long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostException(POST_NOT_FOUND));

        CommentListDTO commentListDTO = getCommentList(post);

        return PostDTO.toPostDTO(post, commentListDTO);
    }

    @Transactional(readOnly = true)
    public CommentListDTO getCommentList(final Post post) {

        List<Comment> commentList =
                post.getCommentList().stream()
                        .filter(comment -> comment.getParent() == null) // 대댓글이 아닌 경우 필터링
                        .sorted(Comparator.comparing(Comment::getCreatedDate)) // 시간순 내림차순 정렬
                        .toList();

        return CommentListDTO.toCommentListDTO(commentList);
    }

    @Transactional
    public void addLikeToPost(final long postId, final long userId) {

        Post post = findPostByIdForUpdate(postId);
        User user = userService.findUserById(userId);

        if (postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
            throw new PostException(LIKE_ALREADY_EXISTS);
        }

        PostLike postLike = PostLike.toPostLikeEntity();
        postLike.setUser(user);
        postLike.setPost(post);

        post.increaseLikeCount();
    }

    @Transactional
    public void removeLikeFromPost(final long postId, final long userId) {

        Post post = findPostByIdForUpdate(postId);

        PostLike postLike =
                postLikeRepository
                        .findByPostIdAndUserId(postId, userId)
                        .orElseThrow(() -> new PostException(LIKE_NOT_FOUND));

        postLikeRepository.delete(postLike);

        post.decreaseLikeCount();
    }

    @Transactional
    public void addLikeToComment(final long commentId, final long userId) {

        Comment comment = findCommentByIdForUpdate(commentId);
        User user = userService.findUserById(userId);

        if (commentLikeRepository.existsByCommentIdAndUserId(commentId, userId)) {
            throw new PostException(LIKE_ALREADY_EXISTS);
        }
        CommentLike commentLike = CommentLike.toCommentLikeEntity();
        commentLike.setUser(user);
        commentLike.setComment(comment);

        comment.increaseLikeCount();
    }

    @Transactional
    public void removeLikeFromComment(final long commentId, final long userId) {

        Comment comment = findCommentByIdForUpdate(commentId);

        CommentLike commentLike =
                commentLikeRepository
                        .findByCommentIdAndUserId(commentId, userId)
                        .orElseThrow(() -> new PostException(LIKE_NOT_FOUND));

        commentLikeRepository.delete(commentLike);

        comment.decreaseLikeCount();
    }

    @Transactional
    public void addCommentToPost(final long userId, final long postId, final String content) {

        Post post = findPostByIdForUpdate(postId);

        User user = userService.findUserById(userId);

        Comment comment = Comment.toCommentEntity(content);
        comment.setUser(user);
        comment.setPost(post);

        post.increaseCommentCount();
    }

    @Transactional
    public void removeCommentFromPost(final long userId, final long postId, final long commentId) {
        Post post = findPostByIdForUpdate(postId);

        Comment comment =
                commentRepository
                        .findByIdAndCommenterId(commentId, userId)
                        .orElseThrow(() -> new PostException(COMMENT_NOT_FOUND));
        Comment parent = comment.getParent();

        if (comment.isDeleted()) { // 이미 삭제된 댓글인 경우
            throw new PostException(COMMENT_NOT_FOUND);
        }

        if (parent == null) { // 부모 댓글인 경우
            if (comment.getChildren().isEmpty()) {
                commentRepository.delete(comment);
            } else {
                comment.setDeleted(true); // 삭제 처리만 하고 실제 DB에서 삭제하지 않음
            }
        } else { // 자식 댓글인 경우
            commentRepository.delete(comment);
            parent.decreaseReplyCount();

            if (parent.getChildren().size() == 1 && parent.isDeleted()) { // 부모 댓글이 삭제 처리된 경우
                commentRepository.delete(parent);
            }
        }

        post.decreaseCommentCount(); // 댓글 수 감소
    }

    @Transactional
    public void addReplyToComment(
            final long userId, final long postId, final long commentId, final String content) {

        Post post = findPostByIdForUpdate(postId);
        User user = userService.findUserById(userId);

        Comment parent =
                commentRepository
                        .findByIdAndCommenterId(commentId, userId)
                        .orElseThrow(() -> new PostException(COMMENT_NOT_FOUND));

        Comment comment = Comment.toCommentEntity(content);
        comment.setUser(user);
        comment.setPost(post);
        comment.setParent(parent);

        post.increaseCommentCount();
        parent.increaseReplyCount();
    }

    @Transactional
    public void updateComment(final long userId, final long commentId, final String content) {
        Comment comment =
                commentRepository
                        .findByIdAndCommenterId(commentId, userId)
                        .orElseThrow(() -> new PostException(COMMENT_NOT_FOUND));

        comment.setContent(content);
    }

    @Transactional
    public void addScrapToPost(final long postId, final long userId) {
        Post post =
                postRepository.findById(postId).orElseThrow(() -> new PostException(POST_NOT_FOUND));
        User user = userService.findUserById(userId);
        log.info("addScrapToPost: post = {}, user = {}", post, user);

        if (postScrapRepository.existsByPostIdAndUserId(postId, userId)) {
            throw new PostException(SCRAP_ALREADY_EXISTS);
        }

        PostScrap postScrap = PostScrap.toPostScrapEntity();
        postScrap.setUser(user);
        postScrap.setPost(post);

        post.increaseScrapCount();
    }

    @Transactional
    public void removeScrapFromPost(final long postId, final long userId) {
        Post post = findPostById(postId);

        PostScrap postScrap =
                postScrapRepository
                        .findByPostIdAndUserId(postId, userId)
                        .orElseThrow(() -> new PostException(SCRAP_NOT_FOUND));

        postScrapRepository.delete(postScrap);

        post.decreaseScrapCount();
    }

    @Transactional
    public Post findPostById(final long id) {
        return postRepository.findById(id).orElseThrow(() -> new PostException(POST_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Post findPostByIdForUpdate(final long id) {
        return postRepository
                .findByIdForUpdate(id)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Comment findCommentByIdForUpdate(final long id) {
        return commentRepository
                .findByIdForUpdate(id)
                .orElseThrow(() -> new PostException(COMMENT_NOT_FOUND));
    }
}
