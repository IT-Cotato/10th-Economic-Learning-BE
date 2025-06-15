package com.ripple.BE.post.application.impl.post;

import static com.ripple.BE.post.exception.errorcode.PostErrorCode.*;
import static com.ripple.BE.user.exception.errorcode.UserErrorCode.*;

import com.ripple.BE.global.config.cache.PostCacheKeyGenerator;
import com.ripple.BE.image.dto.response.ImageResponse;
import com.ripple.BE.image.repository.ImageRepository;
import com.ripple.BE.post.application.CommentQueryUseCase;
import com.ripple.BE.post.application.PostQueryUseCase;
import com.ripple.BE.post.domain.post.Post;
import com.ripple.BE.post.domain.type.PostSort;
import com.ripple.BE.post.domain.type.PostType;
import com.ripple.BE.post.dto.response.CommentResponseDTO;
import com.ripple.BE.post.dto.response.PostPreviewListResponseDTO;
import com.ripple.BE.post.dto.response.PostPreviewResponseDTO;
import com.ripple.BE.post.dto.response.PostResponseDTO;
import com.ripple.BE.post.exception.PostException;
import com.ripple.BE.post.persistence.PostLikeRepository;
import com.ripple.BE.post.persistence.PostRepository;
import com.ripple.BE.post.persistence.PostScrapRepository;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.repository.UserRepository;
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
    private final PostScrapRepository postScrapRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    private final CommentQueryUseCase commentQueryUseCase;

    private static final int PAGE_SIZE = 10;

    @Override
    @Cacheable(
            value = PostCacheKeyGenerator.CACHE_NAME_POSTS,
            keyGenerator = PostCacheKeyGenerator.POST_CACHE_KEY_GENERATOR)
    public PostPreviewListResponseDTO getPosts(
            final int page, final PostSort sort, final PostType type, final long userId) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        if (type == PostType.ECONOMY_TALK) {
            throw new PostException(POST_TYPE_NOT_SUPPORTED);
        }

        Page<Post> postPage =
                (type == null)
                        ? postRepository.findPosts(pageable, sort)
                        : postRepository.findByType(type, sort, pageable);

        List<PostPreviewResponseDTO> previews =
                postPage.getContent().stream().map(post -> toPreview(post, userId)).toList();

        return PostPreviewListResponseDTO.of(previews, postPage.getTotalPages(), postPage.getNumber());
    }

    @Override
    @Cacheable(
            value = PostCacheKeyGenerator.CACHE_NAME_POPULAR_POSTS,
            keyGenerator = PostCacheKeyGenerator.POST_CACHE_KEY_GENERATOR)
    public List<PostPreviewResponseDTO> getPopularPosts(final long userId) {
        return postRepository.findPopularPosts().stream().map(post -> toPreview(post, userId)).toList();
    }

    @Override
    @Cacheable(
            value = PostCacheKeyGenerator.CACHE_NAME_POST_SEARCH,
            keyGenerator = PostCacheKeyGenerator.POST_CACHE_KEY_GENERATOR)
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
        User author =
                userRepository
                        .findById(post.getAuthorId())
                        .orElseThrow(() -> new PostException(USER_NOT_FOUND));

        boolean isAuthor = post.isOwnedBy(userId);
        boolean isLiked = postLikeRepository.existsByPostIdAndUserId(postId, userId);
        boolean isScrapped = postScrapRepository.existsByPostIdAndUserId(postId, userId);

        List<ImageResponse> imageResponses =
                imageRepository.findByPostId(postId).stream().map(ImageResponse::from).toList();
        List<CommentResponseDTO> commentDTOs = commentQueryUseCase.getComments(postId, userId);

        return PostResponseDTO.of(
                post, author, imageResponses, commentDTOs, isScrapped, isLiked, isAuthor);
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
}
