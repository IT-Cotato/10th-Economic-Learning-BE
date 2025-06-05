package com.ripple.BE.user.controller;

import static com.ripple.BE.global.exception.errorcode.GlobalErrorCode.*;

import com.ripple.BE.global.dto.response.ApiResponse;
import com.ripple.BE.news.dto.NewsListDTO;
import com.ripple.BE.news.dto.response.NewsListResponse;
import com.ripple.BE.post.dto.response.PostPreviewResponseDTO;
import com.ripple.BE.term.dto.TermListDTO;
import com.ripple.BE.term.dto.response.TermListResponse;
import com.ripple.BE.term.exception.TermException;
import com.ripple.BE.user.domain.CustomUserDetails;
import com.ripple.BE.user.dto.UserCommentListDTO;
import com.ripple.BE.user.dto.UserCompletedDTO;
import com.ripple.BE.user.dto.UserGoalDTO;
import com.ripple.BE.user.dto.UserInfoDTO;
import com.ripple.BE.user.dto.request.PatchUserProfileRequest;
import com.ripple.BE.user.dto.request.UpdateUserProfileRequest;
import com.ripple.BE.user.dto.request.UserGoalRequest;
import com.ripple.BE.user.dto.response.UserCommentListResponse;
import com.ripple.BE.user.dto.response.UserCompletedResponse;
import com.ripple.BE.user.dto.response.UserCompletionRateByLevelDTOResponse;
import com.ripple.BE.user.dto.response.UserGoalResponse;
import com.ripple.BE.user.dto.response.UserInfoResponse;
import com.ripple.BE.user.service.MyPageService;
import com.ripple.BE.user.service.UserService;
import com.ripple.BE.user.service.UserStatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "User", description = "유저 API")
public class UserController {

    private final UserService userService;
    private final MyPageService myPageService;
    private final UserStatService userStatService;

    @Operation(
            summary = "프로필 등록",
            description =
                    "로그인 후 유저의 프로필을 등록합니다."
                            + "닉네임, 업종, 직업, 생일은 필수입니다. 닉네임은 중복 불가능합니다. (2~10)"
                            + "프로필을 등록하기 전 이미지 등록 후 반한 된 이미지 ID를 입력해주세요.")
    @PostMapping("/profile")
    public ResponseEntity<ApiResponse<?>> profile(
            @Valid @RequestBody UpdateUserProfileRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        userService.updateProfile(request, customUserDetails.getId());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.EMPTY_RESPONSE);
    }

    @Operation(summary = "푸시 알림 설정", description = "로그인 후 유저의 푸시 알림 설정을 변경합니다.")
    @PostMapping("/alarm")
    public ResponseEntity<ApiResponse<?>> alarm(
            @RequestParam boolean alarm, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        userService.updateAlarm(alarm, customUserDetails.getId());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.EMPTY_RESPONSE);
    }

    @Operation(
            summary = "유저가 쓴 게시물 조회",
            description = "유저가 작성한 게시물을 조회합니다. 유저 ID를 입력하지 않으면 로그인한 유저의 게시물을 조회합니다.")
    @GetMapping("/posts")
    public ResponseEntity<ApiResponse<Object>> getMyPosts(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(required = false) Long userId) {

        Long targetUserId = (userId == null) ? customUserDetails.getId() : userId;

        List<PostPreviewResponseDTO> myPosts = myPageService.getMyPosts(targetUserId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(myPosts));
    }

    @Operation(summary = "내가 좋아요한 게시물 조회", description = "로그인한 유저가 좋아요한 게시물을 조회합니다.")
    @GetMapping("/like-posts")
    public ResponseEntity<ApiResponse<Object>> getMyLikePosts(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        List<PostPreviewResponseDTO> postPreviewResponseDTOList =
                myPageService.getMyLikePosts(customUserDetails.getId());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(postPreviewResponseDTOList));
    }

    @Operation(
            summary = "유저가 댓글 단 게시물 조회",
            description = "유저가 댓글을 단 게시물을 조회합니다. 유저 ID를 입력하지 않으면 로그인한 유저의 댓글을 조회합니다.")
    @GetMapping("/comment-posts")
    public ResponseEntity<ApiResponse<Object>> getMyCommentPosts(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(required = false) Long userId) {

        Long targetUserId = (userId == null) ? customUserDetails.getId() : userId;
        UserCommentListDTO myCommentPosts = myPageService.getMyCommentPosts(targetUserId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(UserCommentListResponse.toUserCommentListResponse(myCommentPosts)));
    }

    @Operation(summary = "내가 스크랩한 게시물 조회", description = "로그인한 유저가 스크랩한 게시물을 조회합니다.")
    @GetMapping("/scrap-posts")
    public ResponseEntity<ApiResponse<Object>> getMyScrapPosts(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        List<PostPreviewResponseDTO> postPreviewResponseDTOList =
                myPageService.getMyScrapPosts(customUserDetails.getId());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(postPreviewResponseDTOList));
    }

    @Operation(
            summary = "레벨별 학습 진도율 조회",
            description = "레베벨 진도율을 조회합니다. 100퍼센트 중 몇 퍼센트를 완료했는지 반환합니다.")
    @GetMapping("/progress")
    public ResponseEntity<ApiResponse<Object>> getUserLearningProgress(
            final @AuthenticationPrincipal CustomUserDetails currentUser) {

        UserCompletionRateByLevelDTOResponse dtoResponse =
                userStatService.getLearningSetCompletionRate(currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(dtoResponse));
    }

    @Operation(summary = "내가 스크랩한 뉴스 조회", description = "로그인한 유저가 스크랩한 뉴스를 조회합니다.")
    @GetMapping("/scrap-news")
    public ResponseEntity<ApiResponse<Object>> getMyScrapNews(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        NewsListDTO newsListDTO = myPageService.getMyScrapNews(customUserDetails.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(NewsListResponse.toNewsListResponse(newsListDTO)));
    }

    @Operation(
            summary = "내가 스크랩한 용어 조회",
            description = "로그인한 유저가 스크랩한 용어를 조회합니다. 자음 별로 조회할 수 있으며, 아무것도 입력하지 않으면 모든 용어를 조회합니다.")
    @GetMapping("/scrap-terms")
    public ResponseEntity<ApiResponse<Object>> getMyScrapTermsByInitial(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(value = "initial", required = false) final String initial) {

        if (initial != null && initial.length() != 1) {
            throw new TermException(INVALID_PARAMETER);
        }

        TermListDTO termListDTO =
                myPageService.getMyScrapTermsByInitial(customUserDetails.getId(), initial);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(TermListResponse.toTermListResponse(termListDTO)));
    }

    @Operation(
            summary = "내가 스크랩한 용어 조회",
            description = "로그인한 유저가 스크랩한 용어를 조회합니다. 키워드 별로 조회할 수 있으며, 아무것도 입력하지 않으면 모든 용어를 조회합니다.")
    @GetMapping("/scrap-terms/search")
    public ResponseEntity<ApiResponse<Object>> getMyScrapTermsByKeyword(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(value = "keyword", required = false) final String keyword) {

        TermListDTO termListDTO =
                myPageService.getMyScrapTermsByKeyword(customUserDetails.getId(), keyword);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(TermListResponse.toTermListResponse(termListDTO)));
    }

    @Operation(
            summary = "회원 정보 조회",
            description =
                    "로그인한 유저 또는 다른 유저의 회원 정보를 조회합니다."
                            + "유저 ID를 입력하지 않으면 로그인한 유저의 정보를 조회합니다."
                            + " 프로필 사진 URL, 닉네임, 한줄소개, 생일, 업종, 직업, 연속 출석 일수, 레벨, 퀴즈 정답률을 반환합니다.")
    @GetMapping("/info")
    public ResponseEntity<ApiResponse<Object>> getUserInfo(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(required = false) Long userId) {

        Long targetUserId = (userId == null) ? customUserDetails.getId() : userId;

        UserInfoDTO userInfo = userService.getUserInfo(targetUserId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(UserInfoResponse.toUserInfoResponse(userInfo)));
    }

    @Operation(summary = "사용자 퀘스트 목표 조회", description = "로그인한 유저의 퀘스트 목표를 조회합니다.")
    @GetMapping("/goal")
    public ResponseEntity<ApiResponse<Object>> getUserGoal(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        UserGoalDTO userGoal = userService.getUserGoal(customUserDetails.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(UserGoalResponse.toUserGoalResponse(userGoal)));
    }

    @Operation(summary = "사용자 퀘스트 목표 수정", description = "로그인한 유저의 퀘스트 목표를 수정합니다. 목표는 1 이상이어야 합니다.")
    @PostMapping("/goal")
    public ResponseEntity<ApiResponse<?>> updateUserGoal(
            @Valid @RequestBody UserGoalRequest userGoalRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        userService.updateUserGoal(userGoalRequest, customUserDetails.getId());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.EMPTY_RESPONSE);
    }

    @Operation(summary = "유저 프로필 수정", description = "로그인한 유저의 프로필을 수정합니다. 수정할 필드만 입력하면 됩니다. ")
    @PatchMapping("/profile")
    public ResponseEntity<ApiResponse<?>> patchUserProfile(
            @Valid @RequestBody PatchUserProfileRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        userService.patchUserProfile(request, customUserDetails.getId());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.EMPTY_RESPONSE);
    }

    @Operation(summary = "유저가 완료한 학습과 퀴즈 갯수 반환", description = "로그인한 유저가 완료한 학습과 퀴즈 갯수를 반환합니다.")
    @GetMapping("/completed")
    public ResponseEntity<ApiResponse<Object>> getCompletedConceptAndQuizCount(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        UserCompletedDTO completedDTO =
                myPageService.getCompletedConceptAndQuizCount(customUserDetails.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(UserCompletedResponse.toUserCompletedResponse(completedDTO)));
    }

    @Operation(
            summary = "유저가 참여한 경제 톡톡 조회",
            description = "유저가 참여한 경제 톡톡을 조회합니다. 유저 ID를 입력하지 않으면 로그인한 유저의 톡톡을 조회합니다.")
    @GetMapping("/toktok")
    public ResponseEntity<ApiResponse<Object>> getMyToktok(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(required = false) Long userId) {

        Long targetUserId = (userId == null) ? customUserDetails.getId() : userId;

        List<PostPreviewResponseDTO> myToktok = myPageService.getMyToktok(targetUserId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(myToktok));
    }
}
