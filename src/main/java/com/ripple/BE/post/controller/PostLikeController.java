package com.ripple.BE.post.controller;

import com.ripple.BE.global.dto.response.ApiResponse;
import com.ripple.BE.post.application.PostLikeUseCase;
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
public class PostLikeController {

    private final PostLikeUseCase postLikeUseCase;

    @Operation(summary = "게시물 좋아요", description = "게시물에 좋아요를 누릅니다.")
    @PostMapping("/{id}/like")
    public ResponseEntity<ApiResponse<Object>> addLikeToPost(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("id") long id) {

        postLikeUseCase.addLikeToPost(id, currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }

    @Operation(summary = "게시물 좋아요 취소", description = "게시물에 좋아요를 취소합니다.")
    @DeleteMapping("/{id}/like")
    public ResponseEntity<ApiResponse<Object>> removeLikeFromPost(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("id") long id) {

        postLikeUseCase.removeLikeFromPost(id, currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }
}
