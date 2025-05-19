package com.ripple.BE.post.controller;

import com.ripple.BE.global.dto.response.ApiResponse;
import com.ripple.BE.post.dto.request.CommentRequest;
import com.ripple.BE.post.service.CommentCommandUseCase;
import com.ripple.BE.user.domain.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/post")
@Tag(name = "Post", description = "커뮤니티 게시물 API")
public class CommentController {

    private final CommentCommandUseCase commentCommandUseCase;

    @Operation(summary = "게시물 댓글 추가", description = "게시물에 댓글을 추가합니다.")
    @PostMapping("/{id}/comments")
    public ResponseEntity<ApiResponse<Object>> addComment(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("id") long id,
            final @Valid @RequestBody CommentRequest request) {

        commentCommandUseCase.addCommentToPost(currentUser.getId(), id, request.content());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }

    @Operation(summary = "게시물 댓글 삭제", description = "게시물 댓글을 삭제합니다.")
    @DeleteMapping("/{id}/comments/{commentId}")
    public ResponseEntity<ApiResponse<Object>> deleteComment(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("id") long id,
            final @PathVariable("commentId") long commentId) {

        commentCommandUseCase.removeCommentFromPost(currentUser.getId(), id, commentId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }

    @Operation(summary = "게시물 댓글 답글 추가", description = "게시물 댓글에 답글을 추가합니다.")
    @PostMapping("/{id}/comments/{commentId}/reply")
    public ResponseEntity<ApiResponse<Object>> addReply(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("id") long id,
            final @PathVariable("commentId") long commentId,
            final @Valid @RequestBody CommentRequest request) {

        commentCommandUseCase.addReplyToComment(currentUser.getId(), id, commentId, request.content());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }

    @Operation(summary = "게시물 댓글 수정", description = "게시물 댓글을 수정합니다.")
    @PatchMapping("/{id}/comments/{commentId}")
    public ResponseEntity<ApiResponse<Object>> updateComment(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("id") long id,
            final @PathVariable("commentId") long commentId,
            final @Valid @RequestBody CommentRequest request) {

        commentCommandUseCase.updateComment(currentUser.getId(), id, commentId, request.content());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }
}
