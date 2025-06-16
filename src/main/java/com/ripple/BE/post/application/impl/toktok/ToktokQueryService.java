package com.ripple.BE.post.application.impl.toktok;

import static com.ripple.BE.post.exception.errorcode.PostErrorCode.*;

import com.ripple.BE.image.dto.response.ImageResponse;
import com.ripple.BE.image.persistence.ImageRepository;
import com.ripple.BE.post.application.CommentQueryUseCase;
import com.ripple.BE.post.application.ToktokQueryUseCase;
import com.ripple.BE.post.domain.post.Post;
import com.ripple.BE.post.domain.type.PostSort;
import com.ripple.BE.post.dto.response.CommentResponseDTO;
import com.ripple.BE.post.dto.response.ToktokPreviewListResponseDTO;
import com.ripple.BE.post.dto.response.ToktokPreviewResponseDTO;
import com.ripple.BE.post.dto.response.ToktokResponseDTO;
import com.ripple.BE.post.exception.PostException;
import com.ripple.BE.post.persistence.PostLikeRepository;
import com.ripple.BE.post.persistence.PostScrapRepository;
import com.ripple.BE.post.persistence.ToktokRepository;
import com.ripple.BE.post.persistence.dto.ToktokWithScrapAndImageDTO;
import com.ripple.BE.user.domain.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
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
    private final PostLikeRepository postLikeRepository;
    private final PostScrapRepository postScrapRepository;
    private final ImageRepository imageRepository;

    private final CommentQueryUseCase commentQueryUseCase;

    private static final int PAGE_SIZE = 10;
    private static final int RANDOM_USER_COUNT = 4;

    @Override
    public ToktokPreviewResponseDTO getTodayToktok(final long userId) {

        ToktokWithScrapAndImageDTO toktokWithScrapAndImageDTO =
                toktokRepository
                        .findByUsedDate(LocalDate.now(), userId)
                        .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        List<User> users = toktokRepository.findUsersByToktokPostId(toktokWithScrapAndImageDTO.id());
        List<User> randomUsers = users.size() > RANDOM_USER_COUNT ? getRandomUsers(users) : users;

        return ToktokPreviewResponseDTO.of(toktokWithScrapAndImageDTO, randomUsers);
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

        List<CommentResponseDTO> commentDTOs = commentQueryUseCase.getComments(toktok.getId(), userId);

        return ToktokResponseDTO.of(toktok, imageResponses, commentDTOs, isScrapped, isLiked);
    }

    @Override
    public ToktokPreviewListResponseDTO getToktoks(
            final int page, final PostSort sort, final long userId) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<ToktokWithScrapAndImageDTO> postPage =
                toktokRepository.findUsedToktokPosts(pageable, sort, userId);

        // 1. 게시글 ID 목록 추출
        List<Long> postIds =
                postPage.getContent().stream().map(ToktokWithScrapAndImageDTO::id).toList();

        // 2. 게시글 ID별 유저 목록 한 번에 조회
        Map<Long, List<User>> usersByPostId = toktokRepository.findUsersByToktokPostIds(postIds);

        // 3. DTO로 매핑 (랜덤 4명)
        List<ToktokPreviewResponseDTO> previews =
                postPage.getContent().stream()
                        .map(
                                dto -> {
                                    List<User> users = usersByPostId.getOrDefault(dto.id(), List.of());
                                    List<User> randomUsers = users.size() > 4 ? getRandomUsers(users) : users;
                                    return ToktokPreviewResponseDTO.of(dto, randomUsers);
                                })
                        .toList();

        return ToktokPreviewListResponseDTO.of(
                previews, postPage.getTotalPages(), postPage.getNumber());
    }

    public ToktokPreviewListResponseDTO searchToktoks(
            final String keyword, final int page, final long userId) {

        // 1. 페이지 요청 생성
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<ToktokWithScrapAndImageDTO> postPage =
                toktokRepository.searchUsedToktokPosts(keyword, pageable, userId);

        // 2. 게시글 ID 목록 추출
        List<Long> postIds =
                postPage.getContent().stream().map(ToktokWithScrapAndImageDTO::id).toList();

        // 3. 게시글 ID별 유저 목록 한 번에 조회
        Map<Long, List<User>> usersByPostId = toktokRepository.findUsersByToktokPostIds(postIds);

        // 4. DTO로 매핑 (랜덤 4명)
        List<ToktokPreviewResponseDTO> previews =
                postPage.getContent().stream()
                        .map(
                                dto -> {
                                    List<User> users = usersByPostId.getOrDefault(dto.id(), List.of());
                                    List<User> randomUsers = users.size() > 4 ? getRandomUsers(users) : users;
                                    return ToktokPreviewResponseDTO.of(dto, randomUsers);
                                })
                        .toList();

        return ToktokPreviewListResponseDTO.of(
                previews, postPage.getTotalPages(), postPage.getNumber());
    }

    private List<User> getRandomUsers(List<User> users) {
        List<User> randomUsers = new ArrayList<>(users);
        Collections.shuffle(randomUsers);
        return randomUsers.subList(0, RANDOM_USER_COUNT);
    }
}
