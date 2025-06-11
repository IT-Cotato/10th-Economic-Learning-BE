package com.ripple.BE.user.controller;

import com.ripple.BE.global.dto.response.ApiResponse;
import com.ripple.BE.learning.dto.response.quiz.FailQuizResponseDTO;
import com.ripple.BE.learning.dto.response.scrap.ScrapConceptResponseDTO;
import com.ripple.BE.learning.dto.response.scrap.ScrapQuizResponseDTO;
import com.ripple.BE.post.dto.response.LikeCommentResponseDTO;
import com.ripple.BE.user.domain.CustomUserDetails;
import com.ripple.BE.user.domain.type.Level;
import com.ripple.BE.user.service.MyPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "MyPage", description = "마이페이지 API")
public class MyPageController {

    private final MyPageService myPageService;

    @Operation(summary = "내가 스크랩한 퀴즈 조회", description = "로그인한 유저가 스크랩한 퀴즈를 조회합니다.")
    @GetMapping("/scrap-quizzes")
    public ResponseEntity<ApiResponse<Object>> getMyScrapQuizzes(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(defaultValue = "BEGINNER") Level level) {

        List<ScrapQuizResponseDTO> myScrapQuizzes =
                myPageService.getMyScrapQuizzes(customUserDetails.getId(), level);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(myScrapQuizzes));
    }

    @Operation(summary = "내가 스크랩한 개념 학습 조회", description = "로그인한 유저가 스크랩한 학습을 조회합니다.")
    @GetMapping("/scrap-concepts")
    public ResponseEntity<ApiResponse<Object>> getMyScrapConcepts(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(defaultValue = "BEGINNER") Level level) {

        List<ScrapConceptResponseDTO> myScrapConcepts =
                myPageService.getMyConcepts(customUserDetails.getId(), level);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(myScrapConcepts));
    }

    @Operation(summary = "틀린 문제 조회", description = "로그인 한 유저가 틀렸던 문제를 조회합니다.")
    @GetMapping("/wrong-quizzes")
    public ResponseEntity<ApiResponse<Object>> getWrongQuizzes(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            final @RequestParam(defaultValue = "BEGINNER") Level level) {

        List<FailQuizResponseDTO> failQuizListDTO =
                myPageService.getMyFailQuizzes(customUserDetails.getId(), level);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(failQuizListDTO));
    }

    @Operation(summary = "내가 좋아요한 댓글 조회", description = "로그인한 유저가 좋아요한 댓글을 조회합니다.")
    @GetMapping("/like-comments")
    public ResponseEntity<ApiResponse<Object>> getMyLikeComments(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        List<LikeCommentResponseDTO> myLikeComments =
                myPageService.getMyLikeComments(customUserDetails.getId());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(myLikeComments));
    }
}
