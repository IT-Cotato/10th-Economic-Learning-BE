package com.ripple.BE.post.controller;

import com.ripple.BE.global.dto.response.ApiResponse;
import com.ripple.BE.post.application.PostCommandUseCase;
import com.ripple.BE.post.application.PostQueryUseCase;
import com.ripple.BE.post.application.command.CreatePostCommand;
import com.ripple.BE.post.application.command.UpdatePostCommand;
import com.ripple.BE.post.domain.type.PostSort;
import com.ripple.BE.post.domain.type.PostType;
import com.ripple.BE.post.dto.request.PostRequest;
import com.ripple.BE.post.dto.request.PostUpdateRequest;
import com.ripple.BE.post.dto.response.PostPreviewListResponseDTO;
import com.ripple.BE.post.dto.response.PostPreviewResponseDTO;
import com.ripple.BE.post.dto.response.PostResponseDTO;
import com.ripple.BE.user.domain.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/post")
@Tag(name = "Post", description = "커뮤니티 게시물 API")
public class PostController {

    private final PostCommandUseCase postCommandUseCase;
    private final PostQueryUseCase postQueryUseCase;

    @Operation(
            summary = "게시물 작성",
            description =
                    "게시물을 작성합니다. 게시물을 등록하기 전 이미지 등록을 완료해주세요. 이미지 등록 후 반한 된 이미지 ID를 입력해주세요."
                            + "게시물 타입은 FREE ,QUESTION, INFORMATION, BOOK_RECOMMENDATION 중 하나여야 합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createPost(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @Valid @RequestBody PostRequest request) {

        CreatePostCommand createPostCommand =
                CreatePostCommand.of(
                        currentUser.getId(),
                        request.title(),
                        request.content(),
                        request.type(),
                        request.imageIds());

        postCommandUseCase.createPost(createPostCommand);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }

    @Operation(
            summary = "게시물 수정",
            description =
                    "게시물을 수정합니다. 게시물을 수정하기 전 이미지 수정을 완료해주세요. 삭제한 이미지는 입력하지 말고, 새로 추가된 이미지의 ID만 입력해주세요.")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> updatePost(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("id") long id,
            final @Valid @RequestBody PostUpdateRequest request) {

        UpdatePostCommand updatePostCommand =
                UpdatePostCommand.of(
                        id,
                        currentUser.getId(),
                        request.title(),
                        request.content(),
                        request.type(),
                        request.newImageIds());

        postCommandUseCase.updatePost(updatePostCommand);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }

    @Operation(summary = "게시물 삭제", description = "게시물을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deletePost(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("id") long id) {

        postCommandUseCase.deletePost(id, currentUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }

    @Operation(summary = "게시물 상세 조회", description = "게시물의 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> getPost(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("id") long id) {

        PostResponseDTO postResponseDTO = postQueryUseCase.getPost(id, currentUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(postResponseDTO));
    }

    @Operation(
            summary = "게시물 목록 조회",
            description =
                    "게시물 목록을 조회합니다. 페이지 번호는 0부터 시작하며, 페이지 당 10개의 게시물을 반환합니다. "
                            + "게시물 타입은 FREE ,QUESTION, INFORMATION, BOOK_RECOMMENDATION 중 하나여야 합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getPosts(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(required = false, defaultValue = "RECENT") PostSort sort,
            @RequestParam(required = false) PostType type) {

        PostPreviewListResponseDTO postPreviewListResponseDTO =
                postQueryUseCase.getPosts(page, sort, type);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(postPreviewListResponseDTO));
    }

    @Operation(
            summary = "인기 게시물 목록 조회",
            description = "인기 게시물 목록을 조회합니다. 페이지 번호는 0부터 시작하며, 페이지 당 10개의 게시물을 반환합니다.")
    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<Object>> getPopularPosts() {
        List<PostPreviewResponseDTO> postPreviewResponseDTOList = postQueryUseCase.getPopularPosts();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(postPreviewResponseDTOList));
    }
}
