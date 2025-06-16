package com.ripple.BE.learning.controller;

import com.ripple.BE.global.dto.response.ApiResponse;
import com.ripple.BE.learning.application.learningset.LearningSetService;
import com.ripple.BE.learning.dto.response.learningset.UserLearningSetPreviewResponseDTO;
import com.ripple.BE.user.domain.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/learning")
@Tag(name = "Learning", description = "학습 API")
public class LearningController {

    private final LearningSetService learningSetService;

    @Operation(
            summary = "레벨별 학습 세트 조회",
            description = "레벨별 전체 학습 세트를 조회합니다. 사용자의 현재 레벨에 해당하는 학습 세트 목록을 반환합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getLearningSets(
            final @AuthenticationPrincipal CustomUserDetails currentUser) {

        List<UserLearningSetPreviewResponseDTO> userLearningSetPreviewList =
                learningSetService.getLearningSetPreviewList(currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(userLearningSetPreviewList));
    }
}
