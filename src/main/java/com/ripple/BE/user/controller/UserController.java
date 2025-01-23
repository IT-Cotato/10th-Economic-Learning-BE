package com.ripple.BE.user.controller;

import com.ripple.BE.global.dto.response.ApiResponse;
import com.ripple.BE.learning.dto.FailQuizListDTO;
import com.ripple.BE.learning.dto.QuizListDTO;
import com.ripple.BE.learning.dto.response.FailQuizListResponse;
import com.ripple.BE.learning.dto.response.ScrapQuizListResponse;
import com.ripple.BE.post.dto.LikeCommentListDTO;
import com.ripple.BE.post.dto.PostListDTO;
import com.ripple.BE.post.dto.response.LikeCommentListResponse;
import com.ripple.BE.post.dto.response.PostListResponse;
import com.ripple.BE.user.domain.CustomUserDetails;
import com.ripple.BE.user.domain.type.Level;
import com.ripple.BE.user.dto.ProgressDTO;
import com.ripple.BE.user.dto.ProgressResponse;
import com.ripple.BE.user.dto.UpdateUserProfileRequest;
import com.ripple.BE.user.service.MyPageService;
import com.ripple.BE.user.service.UserProgressService;
import com.ripple.BE.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "User", description = "유저 API")
public class UserController {

    private final UserService userService;
    private final MyPageService myPageService;
    private final UserProgressService userProgressService;

    @Operation(summary = "프로필 등록", description = "로그인 후 유저의 프로필을 등록합니다.")
    @PostMapping("/profile")
    public ResponseEntity<ApiResponse<?>> profile(
            @Valid @RequestBody UpdateUserProfileRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        userService.updateProfile(request, customUserDetails.getId());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.EMPTY_RESPONSE);
    }

    @Operation(summary = "내가 쓴 게시물 조회", description = "로그인한 유저가 작성한 게시물을 조회합니다.")
    @GetMapping("/posts")
    public ResponseEntity<ApiResponse<Object>> getMyPosts(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        PostListDTO postListDTO = myPageService.getMyPosts(customUserDetails.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(PostListResponse.toPostListResponse(postListDTO)));
    }

    @Operation(summary = "내가 좋아요한 게시물 조회", description = "로그인한 유저가 좋아요한 게시물을 조회합니다.")
    @GetMapping("/like-posts")
    public ResponseEntity<ApiResponse<Object>> getMyLikePosts(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        PostListDTO postListDTO = myPageService.getMyLikePosts(customUserDetails.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(PostListResponse.toPostListResponse(postListDTO)));
    }

    @Operation(summary = "내가 댓글 단 게시물 조회", description = "로그인한 유저가 댓글을 단 게시물을 조회합니다.")
    @GetMapping("/comment-posts")
    public ResponseEntity<ApiResponse<Object>> getMyCommentPosts(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        PostListDTO postListDTO = myPageService.getMyCommentPosts(customUserDetails.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(PostListResponse.toPostListResponse(postListDTO)));
    }

    @Operation(summary = "내가 스크랩한 게시물 조회", description = "로그인한 유저가 스크랩한 게시물을 조회합니다.")
    @GetMapping("/scrap-posts")
    public ResponseEntity<ApiResponse<Object>> getMyScrapPosts(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        PostListDTO postListDTO = myPageService.getMyScrapPosts(customUserDetails.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(PostListResponse.toPostListResponse(postListDTO)));
    }

    @Operation(
            summary = "레벨별 학습 진도율 조회",
            description = "레베벨 진도율을 조회합니다. 100퍼센트 중 몇 퍼센트를 완료했는지 반환합니다.")
    @GetMapping("/progress")
    public ResponseEntity<ApiResponse<Object>> getUserLearningProgress(
            final @AuthenticationPrincipal CustomUserDetails currentUser) {

        ProgressDTO progressDTO = userProgressService.getLearningSetCompletionRate(currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(ProgressResponse.toProgressResponse(progressDTO)));
    }

    @Operation(summary = "틀린 문제 조회", description = "로그인 한 유저가 틀렸던 문제를 조회합니다.")
    @GetMapping("/wrong-quizzes")
    public ResponseEntity<ApiResponse<Object>> getWrongQuizzes(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            final @RequestParam(defaultValue = "BEGINNER") Level level) {

        FailQuizListDTO failQuizListDTO =
                myPageService.getMyFailQuizzes(customUserDetails.getId(), level);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(FailQuizListResponse.toFailQuizListResponse(failQuizListDTO)));
    }

    @Operation(summary = "내가 좋아요한 댓글 조회", description = "로그인한 유저가 좋아요한 댓글을 조회합니다.")
    @GetMapping("/like-comments")
    public ResponseEntity<ApiResponse<Object>> getMyLikeComments(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        LikeCommentListDTO myLikeComments = myPageService.getMyLikeComments(customUserDetails.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(LikeCommentListResponse.toLikeCommentListResponse(myLikeComments)));
    }

    @Operation(summary = "내가 스크랩한 퀴즈 조회", description = "로그인한 유저가 스크랩한 퀴즈를 조회합니다.")
    @GetMapping("/scrap-quizzes")
    public ResponseEntity<ApiResponse<Object>> getMyScrapQuizzes(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(defaultValue = "BEGINNER") Level level) {

        QuizListDTO myScrapQuizzes = myPageService.getMyScrapQuizzes(customUserDetails.getId(), level);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(ScrapQuizListResponse.toScrapQuizListResponse(myScrapQuizzes)));
    }
}
