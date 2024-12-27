package com.ripple.BE.learning.controller;

import com.ripple.BE.global.dto.response.ApiResponse;
import com.ripple.BE.learning.dto.LearningSetCompleteListDTO;
import com.ripple.BE.learning.dto.ProgressDTO;
import com.ripple.BE.learning.dto.request.LearningSetRequest;
import com.ripple.BE.learning.dto.response.LearningSetPreviewListResponse;
import com.ripple.BE.learning.dto.response.LearningSetProgressResponse;
import com.ripple.BE.learning.service.LearningAdminService;
import com.ripple.BE.learning.service.LearningSetService;
import com.ripple.BE.user.domain.CustomUserDetails;
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
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/learning")
@Tag(name = "Learning", description = "학습 API")
public class LearningController {

    private final LearningSetService learningSetService;
    private final LearningAdminService learningAdminService;

    @Operation(summary = "학습 세트 생성", description = "엑셀 파일로부터 학습 세트를 생성합니다.")
    @PostMapping("/excel")
    public ResponseEntity<ApiResponse<?>> saveLearningSetsByExcel() {
        learningAdminService.createLearningSetByExcel();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.EMPTY_RESPONSE);
    }

    @Operation(summary = "레벨별 학습 세트 조회", description = "레벨별 전체 학습 세트를 조회합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<Object>> getLearningSets(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @RequestBody @Valid LearningSetRequest request) {

        LearningSetCompleteListDTO learningSetCompleteListDTO =
                learningSetService.getLearningSetPreviewList(currentUser.getId(), request.level());

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.from(
                                LearningSetPreviewListResponse.toLearningSetPreviewListResponse(
                                        learningSetCompleteListDTO)));
    }

    @Operation(
            summary = "학습 세트 진도율 조회",
            description = "학습 세트의 진도율을 조회합니다. 100퍼센트 중 몇 퍼센트를 완료했는지 반환합니다.")
    @GetMapping("/progress")
    public ResponseEntity<ApiResponse<Object>> getLearningSetProgress(
            final @AuthenticationPrincipal CustomUserDetails currentUser) {

        ProgressDTO progressDTO = learningSetService.getLearningSetCompletionRate(currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.from(
                                LearningSetProgressResponse.toLearningSetProgressResponse(progressDTO)));
    }
}
