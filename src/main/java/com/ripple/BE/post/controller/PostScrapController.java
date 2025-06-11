package com.ripple.BE.post.controller;

import com.ripple.BE.global.dto.response.ApiResponse;
import com.ripple.BE.post.application.PostScrapUseCase;
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
public class PostScrapController {

    private final PostScrapUseCase postScrapUseCase;

    @Operation(summary = "게시물 스크랩", description = "게시물을 스크랩합니다.")
    @PostMapping("/{id}/scrap")
    public ResponseEntity<ApiResponse<Object>> addScrapToPost(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("id") long id) {

        postScrapUseCase.addScrapToPost(id, currentUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }

    @Operation(summary = "게시물 스크랩 취소", description = "게시물 스크랩을 취소합니다.")
    @DeleteMapping("/{id}/scrap")
    public ResponseEntity<ApiResponse<Object>> removeScrapFromPost(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("id") long id) {

        postScrapUseCase.removeScrapFromPost(id, currentUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }
}
