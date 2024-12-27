package com.ripple.BE.learning.controller;

import com.ripple.BE.global.dto.response.ApiResponse;
import com.ripple.BE.learning.dto.ConceptListDTO;
import com.ripple.BE.learning.dto.response.ConceptListResponse;
import com.ripple.BE.learning.service.ConceptService;
import com.ripple.BE.user.domain.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/learning")
@Tag(name = "Learning", description = "학습 API")
public class ConceptController {

    private final ConceptService conceptService;

    @Operation(summary = "개념 학습 세트 조회", description = "개념 학습 세트를 조회합니다.")
    @GetMapping("/{learningSetId}/concepts")
    public ResponseEntity<ApiResponse<Object>> getConcepts(
            final @PathVariable("learningSetId") long learningSetId) {

        ConceptListDTO conceptListDTO = conceptService.getConcepts(learningSetId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(ConceptListResponse.toConceptListResponse(conceptListDTO)));
    }

    @Operation(summary = "개념 학습 완료 처리", description = "개념 학습을 완료 처리합니다.")
    @PostMapping("/{learningSetId}/concepts/complete")
    public ResponseEntity<ApiResponse<?>> completeConcept(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("learningSetId") long learningSetId) {

        conceptService.completeConceptLearning(currentUser.getId(), learningSetId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.EMPTY_RESPONSE);
    }
}
