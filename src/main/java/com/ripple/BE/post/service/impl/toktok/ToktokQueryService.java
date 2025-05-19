package com.ripple.BE.post.service.impl.toktok;

import static com.ripple.BE.post.exception.errorcode.PostErrorCode.*;

import com.ripple.BE.image.dto.response.ImageResponse;
import com.ripple.BE.image.repository.ImageRepository;
import com.ripple.BE.post.domain.post.Post;
import com.ripple.BE.post.domain.type.PostSort;
import com.ripple.BE.post.dto.response.CommentResponseDTO;
import com.ripple.BE.post.dto.response.ToktokPreviewListResponseDTO;
import com.ripple.BE.post.dto.response.ToktokPreviewResponseDTO;
import com.ripple.BE.post.dto.response.ToktokResponseDTO;
import com.ripple.BE.post.exception.PostException;
import com.ripple.BE.post.persistence.CommentLikeRepository;
import com.ripple.BE.post.persistence.CommentRepository;
import com.ripple.BE.post.persistence.PostLikeRepository;
import com.ripple.BE.post.persistence.PostScrapRepository;
import com.ripple.BE.post.persistence.ToktokRepository;
import com.ripple.BE.post.service.ToktokQueryUseCase;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.dto.UserRandomProfileListDTO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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
public class ToktokQueryService implements ToktokQueryUseCase {

    private final ToktokRepository toktokRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostScrapRepository postScrapRepository;
    private final CommentLikeRepository commentLikeRepository;

    private final ImageRepository imageRepository;

    private static final int PAGE_SIZE = 10;
    private static final int RANDOM_USER_COUNT = 4;

    @Override
    public ToktokPreviewResponseDTO getTodayToktok(final long userId) {

        Post toktok =
                toktokRepository
                        .findByUsedDate(LocalDate.now())
                        .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        return toPreview(toktok, userId);
    }

    @Override
    public ToktokResponseDTO getToktok(final long id, final long userId) {

        Post toktok =
                toktokRepository.findById(id).orElseThrow(() -> new PostException(POST_NOT_FOUND));

        if (toktok.getUsedDate() == null) {
            throw new PostException(POST_NOT_FOUND);
        }

        boolean isLiked = postLikeRepository.existsByPostIdAndUserId(id, userId);
        boolean isScrapped = postScrapRepository.existsByPostIdAndUserId(id, userId);

        List<ImageResponse> imageResponses =
                imageRepository.findByPostId(id).stream().map(ImageResponse::from).toList();

        List<CommentResponseDTO> commentDTOs = getCommentDTOs(toktok, userId);

        return ToktokResponseDTO.of(toktok, imageResponses, commentDTOs, isScrapped, isLiked);
    }

    @Override
    public ToktokPreviewListResponseDTO getToktoks(
            final int page, final PostSort sort, final long userId) {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        // 게시글 조회 (타입에 따른 필터링)
        Page<Post> postPage = toktokRepository.findUsedToktokPosts(pageable, sort);
        List<ToktokPreviewResponseDTO> previews =
                postPage.getContent().stream().map(post -> toPreview(post, userId)).toList();

        return ToktokPreviewListResponseDTO.of(
                previews, postPage.getTotalPages(), postPage.getNumber());
    }

    @Override
    @Cacheable(value = "toktokSearch", key = "#keyword != null ? #keyword + #page : #page")
    public ToktokPreviewListResponseDTO searchToktoks(
            final String keyword, final int page, final long userId) {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<Post> postPage = toktokRepository.searchUsedToktokPosts(keyword, pageable);

        List<ToktokPreviewResponseDTO> previews =
                postPage.getContent().stream().map(post -> toPreview(post, userId)).toList();

        /** 최근 검색어 추가 로직 필요 */
        return ToktokPreviewListResponseDTO.of(
                previews, postPage.getTotalPages(), postPage.getNumber());
    }

    private ToktokPreviewResponseDTO toPreview(Post toktokPost, long userId) {
        String imageUrl =
                imageRepository.findByPostId(toktokPost.getId()).stream()
                        .findFirst()
                        .map(image -> image.getS3Info().getUrl())
                        .orElse(null);

        List<User> users = toktokRepository.findUsersByToktokPostId(toktokPost.getId());
        boolean isScraped = postScrapRepository.existsByPostIdAndUserId(toktokPost.getId(), userId);
        List<User> randomUsers = users.size() > 4 ? getRandomUsers(users) : users;

        return ToktokPreviewResponseDTO.of(
                toktokPost,
                imageUrl,
                isScraped,
                UserRandomProfileListDTO.toUserRandomProfileListDTO(randomUsers));
    }

    private List<CommentResponseDTO> getCommentDTOs(Post toktokPost, long userId) {
        return commentRepository.findRootCommentsByPost(toktokPost.getId()).stream()
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

    private List<User> getRandomUsers(List<User> users) {
        List<User> randomUsers = new ArrayList<>(users);
        Collections.shuffle(randomUsers);
        return randomUsers.subList(0, RANDOM_USER_COUNT);
    }
}
