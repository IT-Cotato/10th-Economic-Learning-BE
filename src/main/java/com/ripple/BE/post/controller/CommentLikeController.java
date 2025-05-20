package com.ripple.BE.post.controller;

import com.ripple.BE.global.dto.response.ApiResponse;
import com.ripple.BE.post.service.CommentLikeUseCase;
import com.ripple.BE.user.domain.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/post")
@Tag(name = "Post", description = "커뮤니티 게시물 API")
public class CommentLikeController {

    private final CommentLikeUseCase commentLikeUseCase;

    @Operation(summary = "게시물 댓글 좋아요", description = "게시물 댓글에 좋아요를 누릅니다.")
    @PostMapping("/{id}/comments/{commentId}/like")
    public ResponseEntity<ApiResponse<Object>> likeComment(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("id") long id,
            final @PathVariable("commentId") long commentId) {

        commentLikeUseCase.addLikeToComment(commentId, currentUser.getId(), id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }

    @Operation(summary = "게시물 댓글 좋아요 취소", description = "게시물 댓글에 좋아요를 취소합니다.")
    @DeleteMapping("/{id}/comments/{commentId}/like")
    public ResponseEntity<ApiResponse<Object>> unlikeComment(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("id") long id,
            final @PathVariable("commentId") long commentId) {

        commentLikeUseCase.removeLikeFromComment(commentId, currentUser.getId(), id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }
}
