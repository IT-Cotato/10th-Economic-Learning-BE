package com.ripple.BE.post.service.impl.post;

import static com.ripple.BE.post.exception.errorcode.PostErrorCode.*;

import com.ripple.BE.image.dto.response.ImageResponse;
import com.ripple.BE.image.repository.ImageRepository;
import com.ripple.BE.post.domain.post.Post;
import com.ripple.BE.post.domain.type.PostSort;
import com.ripple.BE.post.domain.type.PostType;
import com.ripple.BE.post.dto.response.CommentResponseDTO;
import com.ripple.BE.post.dto.response.PostPreviewListResponseDTO;
import com.ripple.BE.post.dto.response.PostPreviewResponseDTO;
import com.ripple.BE.post.dto.response.PostResponseDTO;
import com.ripple.BE.post.exception.PostException;
import com.ripple.BE.post.persistence.CommentLikeRepository;
import com.ripple.BE.post.persistence.CommentRepository;
import com.ripple.BE.post.persistence.PostLikeRepository;
import com.ripple.BE.post.persistence.PostRepository;
import com.ripple.BE.post.persistence.PostScrapRepository;
import com.ripple.BE.post.service.PostQueryUseCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostQueryService implements PostQueryUseCase {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final PostScrapRepository postScrapRepository;

    private final ImageRepository imageRepository;

    private static final int PAGE_SIZE = 10;

    @Override
    @Cacheable(
            value = "posts",
            key =
                    "#page + (#sort != null ? #sort.toString() : '') + (#type != null ? #type.toString() : '')")
    public PostPreviewListResponseDTO getPosts(
            final int page, final PostSort sort, final PostType type, final long userId) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        Page<Post> postPage =
                (type == null)
                        ? postRepository.findPosts(pageable, sort)
                        : postRepository.findByType(type, sort, pageable);

        List<PostPreviewResponseDTO> previews =
                postPage.getContent().stream().map(post -> toPreview(post, userId)).toList();

        return PostPreviewListResponseDTO.of(previews, postPage.getTotalPages(), postPage.getNumber());
    }

    @Override
    @Cacheable(value = "popularPosts")
    public List<PostPreviewResponseDTO> getPopularPosts(final long userId) {
        return postRepository.findPopularPosts().stream().map(post -> toPreview(post, userId)).toList();
    }

    @Override
    @Cacheable(value = "postSearch", key = "#keyword != null ? #keyword + #page : #page")
    public PostPreviewListResponseDTO searchPosts(
            final String keyword, final int page, final long userId) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        Page<Post> postPage = postRepository.searchNormalPosts(keyword, pageable);
        List<PostPreviewResponseDTO> previews =
                postPage.getContent().stream().map(post -> toPreview(post, userId)).toList();

        /** 추후 검색어에 대한 최근 검색어 저장 로직 추가 */
        return PostPreviewListResponseDTO.of(previews, postPage.getTotalPages(), postPage.getNumber());
    }

    @Override
    public PostResponseDTO getPost(final long postId, final long userId) {
        Post post =
                postRepository.findById(postId).orElseThrow(() -> new PostException(POST_NOT_FOUND));

        boolean isAuthor = post.isOwnedBy(userId);
        boolean isLiked = postLikeRepository.existsByPostIdAndUserId(postId, userId);
        boolean isScrapped = postScrapRepository.existsByPostIdAndUserId(postId, userId);

        List<ImageResponse> imageResponses =
                imageRepository.findByPostId(postId).stream().map(ImageResponse::from).toList();
        List<CommentResponseDTO> commentDTOs = getCommentDTOs(post, userId);

        return PostResponseDTO.of(post, imageResponses, commentDTOs, isScrapped, isLiked, isAuthor);
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

    private List<CommentResponseDTO> getCommentDTOs(Post post, long userId) {
        return commentRepository.findRootCommentsByPost(post.getId()).stream()
                .map(
                        root -> {
                            // 자식 댓글 처리
                            List<CommentResponseDTO> children =
                                    commentRepository.findChildrenByParentId(root.getId()).stream()
                                            .map(
                                                    child -> {
                                                        boolean childIsAuthor = child.getCommenter().getId().equals(userId);
                                                        boolean childIsLiked =
                                                                commentLikeRepository.existsByCommentIdAndUserId(
                                                                        child.getId(), userId);
                                                        return CommentResponseDTO.of(
                                                                child, List.of(), childIsAuthor, childIsLiked);
                                                    })
                                            .toList();

                            boolean rootIsAuthor = root.getCommenter().getId().equals(userId);
                            boolean rootIsLiked =
                                    commentLikeRepository.existsByCommentIdAndUserId(root.getId(), userId);

                            return CommentResponseDTO.of(root, children, rootIsAuthor, rootIsLiked);
                        })
                .toList();
    }
}
