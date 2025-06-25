package com.ripple.BE.post.application.impl.post;

import static com.ripple.BE.post.exception.errorcode.PostErrorCode.*;
import static com.ripple.BE.user.exception.errorcode.UserErrorCode.*;

import com.ripple.BE.image.dto.response.ImageResponse;
import com.ripple.BE.image.persistence.ImageRepository;
import com.ripple.BE.post.application.CommentQueryUseCase;
import com.ripple.BE.post.application.PostQueryUseCase;
import com.ripple.BE.post.application.cache.PostCacheKey;
import com.ripple.BE.post.application.cache.PostCacheManager;
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
import com.ripple.BE.post.persistence.dto.PostWithImageDTO;
import com.ripple.BE.search.service.SearchService;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
    private final SearchService searchService;

    private final PostCacheManager postCacheManager;

    private static final int PAGE_SIZE = 10;

    @Override
    public PostPreviewListResponseDTO getPosts(
            final int page, final PostSort sort, final PostType type) {

        // 1. 경제 톡톡인 경우 예외 처리
        if (type == PostType.ECONOMY_TALK) {
            throw new PostException(POST_TYPE_NOT_SUPPORTED);
        }

        // 2. 캐시에서 조회
        String key = PostCacheKey.generatePostListKey(type, sort, page);
        PostPreviewListResponseDTO cached = postCacheManager.getPosts(key);
        if (cached != null) return cached;

        // 3. 캐시에 없으면 DB에서 조회
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<PostWithImageDTO> postPage =
                (type == null)
                        ? postRepository.findPosts(pageable, sort)
                        : postRepository.findByType(type, sort, pageable);

        List<PostPreviewResponseDTO> previews =
                postPage.getContent().stream().map(PostPreviewResponseDTO::from).toList();
        PostPreviewListResponseDTO result =
                PostPreviewListResponseDTO.of(previews, postPage.getTotalPages(), page);

        // 4. 캐시에 저장
        postCacheManager.putPosts(key, result);
        return result;
    }

    @Override
    public List<PostPreviewResponseDTO> getPopularPosts() {
        String key = PostCacheKey.POPULAR_POSTS;

        // 1. 캐시 조회
        List<PostPreviewResponseDTO> cached = postCacheManager.getPopular(key);
        if (cached != null) return cached;

        // 2. 캐시에 없으면 DB 조회
        List<PostPreviewResponseDTO> result =
                postRepository.findPopularPosts().stream().map(PostPreviewResponseDTO::from).toList();

        // 3. 캐시에 저장
        postCacheManager.putPopular(key, result);

        return result;
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

    public PostPreviewListResponseDTO searchPosts(
            final String keyword, final int page, final long userId) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<PostWithImageDTO> postPage = postRepository.searchNormalPosts(keyword, pageable);

        List<PostPreviewResponseDTO> previews =
                postPage.getContent().stream().map(PostPreviewResponseDTO::from).toList();

        searchService.addRecentSearch(userId, keyword);

        return PostPreviewListResponseDTO.of(previews, postPage.getTotalPages(), postPage.getNumber());
    }
}
