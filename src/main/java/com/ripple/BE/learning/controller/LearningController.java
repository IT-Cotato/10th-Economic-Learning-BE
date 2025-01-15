package com.ripple.BE.learning.controller;

import com.ripple.BE.global.dto.response.ApiResponse;
import com.ripple.BE.learning.dto.UserLearningSetListDTO;
import com.ripple.BE.learning.dto.response.LearningSetPreviewListResponse;
import com.ripple.BE.learning.service.learningset.LearningAdminService;
import com.ripple.BE.learning.service.learningset.LearningSetService;
import com.ripple.BE.user.domain.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
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
            final @AuthenticationPrincipal CustomUserDetails currentUser) {

        UserLearningSetListDTO userLearningSetListDTO =
                learningSetService.getLearningSetPreviewList(currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.from(
                                LearningSetPreviewListResponse.toLearningSetPreviewListResponse(
                                        userLearningSetListDTO)));
    }
}
