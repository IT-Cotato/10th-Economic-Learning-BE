package com.ripple.BE.post.controller;

import com.ripple.BE.global.dto.response.ApiResponse;
import com.ripple.BE.post.domain.type.PostSort;
import com.ripple.BE.post.domain.type.PostType;
import com.ripple.BE.post.dto.PostDTO;
import com.ripple.BE.post.dto.PostListDTO;
import com.ripple.BE.post.dto.request.CommentRequest;
import com.ripple.BE.post.dto.request.PostRequest;
import com.ripple.BE.post.dto.response.PostListResponse;
import com.ripple.BE.post.dto.response.PostResponse;
import com.ripple.BE.post.service.PostService;
import com.ripple.BE.user.domain.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/post")
@Tag(name = "Post", description = "커뮤니티 게시물 API")
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시물 작성", description = "게시물을 작성합니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Object>> createPost(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @RequestPart(value = "post") @Valid PostRequest post,
            final @RequestPart(value = "imageList", required = false) List<MultipartFile> imageList) {

        postService.createPost(currentUser.getId(), PostDTO.toPostDTO(post), imageList);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }

    @Operation(summary = "게시물 삭제", description = "게시물을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deletePost(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("id") long id) {

        postService.deletePost(id, currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }

    @Operation(summary = "게시글 목록 조회", description = "게시글 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getPosts(
            final @RequestParam(required = false, defaultValue = "0") @PositiveOrZero int page,
            final @RequestParam(required = false, defaultValue = "RECENT") PostSort sort,
            final @RequestParam(required = false) PostType type) {

        PostListDTO postListDTO = postService.getPosts(page, sort, type);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(PostListResponse.toPostListResponse(postListDTO)));
    }

    @Operation(summary = "게시물 상세 조회", description = "게시물의 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> getPost(final @PathVariable("id") long id) {

        PostDTO postDTO = postService.getPost(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(PostResponse.toPostResponse(postDTO)));
    }

    @Operation(summary = "게시물 좋아요", description = "게시물에 좋아요를 누릅니다.")
    @PostMapping("/{id}/like")
    public ResponseEntity<ApiResponse<Object>> likePost(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("id") long id) {

        postService.addLikeToPost(id, currentUser.getId());
        ;

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }

    @Operation(summary = "게시물 좋아요 취소", description = "게시물에 좋아요를 취소합니다.")
    @DeleteMapping("/{id}/like")
    public ResponseEntity<ApiResponse<Object>> unlikePost(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("id") long id) {

        postService.removeLikeFromPost(id, currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }

    @Operation(summary = "게시물 댓글 좋아요", description = "게시물 댓글에 좋아요를 누릅니다.")
    @PostMapping("/{id}/comments/{commentId}/like")
    public ResponseEntity<ApiResponse<Object>> likeComment(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("id") long id,
            final @PathVariable("commentId") long commentId) {

        postService.addLikeToComment(commentId, currentUser.getId());
        ;

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }

    @Operation(summary = "게시물 댓글 좋아요 취소", description = "게시물 댓글에 좋아요를 취소합니다.")
    @DeleteMapping("/{id}/comments/{commentId}/like")
    public ResponseEntity<ApiResponse<Object>> unlikeComment(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("id") long id,
            final @PathVariable("commentId") long commentId) {

        postService.removeLikeFromComment(commentId, currentUser.getId());
        ;

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }

    @Operation(summary = "게시물 댓글 추가", description = "게시물에 댓글을 추가합니다.")
    @PostMapping("/{id}/comments")
    public ResponseEntity<ApiResponse<Object>> addComment(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("id") long id,
            final @Valid @RequestBody CommentRequest request) {

        postService.addCommentToPost(currentUser.getId(), id, request.content());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }

    @Operation(summary = "게시물 댓글 삭제", description = "게시물 댓글을 삭제합니다.")
    @DeleteMapping("/{id}/comments/{commentId}")
    public ResponseEntity<ApiResponse<Object>> deleteComment(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("id") long id,
            final @PathVariable("commentId") long commentId) {

        postService.removeCommentFromPost(currentUser.getId(), id, commentId);
        ;
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }

    @Operation(summary = "게시물 댓글 답글 추가", description = "게시물 댓글에 답글을 추가합니다.")
    @PostMapping("/{id}/comments/{commentId}/reply")
    public ResponseEntity<ApiResponse<Object>> addReply(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("id") long id,
            final @PathVariable("commentId") long commentId,
            final @Valid @RequestBody CommentRequest request) {

        postService.addReplyToComment(currentUser.getId(), id, commentId, request.content());
        ;

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }

    @Operation(summary = "게시물 댓글 수정", description = "게시물 댓글을 수정합니다.")
    @PatchMapping("/{id}/comments/{commentId}")
    public ResponseEntity<ApiResponse<Object>> updateComment(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("id") long id,
            final @PathVariable("commentId") long commentId,
            final @Valid @RequestBody CommentRequest request) {

        postService.updateComment(currentUser.getId(), commentId, request.content());
        ;

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }

    @Operation(summary = "게시물 스크랩", description = "게시물을 스크랩합니다.")
    @PostMapping("/{id}/scrap")
    public ResponseEntity<ApiResponse<Object>> scrapPost(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("id") long id) {

        postService.addScrapToPost(id, currentUser.getId());
        ;

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }

    @Operation(summary = "게시물 스크랩 취소", description = "게시물 스크랩을 취소합니다.")
    @DeleteMapping("/{id}/scrap")
    public ResponseEntity<ApiResponse<Object>> unscrapPost(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("id") long id) {

        postService.removeScrapFromPost(id, currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }
}
