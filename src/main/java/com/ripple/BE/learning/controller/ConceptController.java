package com.ripple.BE.learning.controller;

import com.ripple.BE.global.dto.response.ApiResponse;
import com.ripple.BE.learning.dto.ConceptDTO;
import com.ripple.BE.learning.dto.ConceptListDTO;
import com.ripple.BE.learning.dto.response.ConceptDetailResponse;
import com.ripple.BE.learning.dto.response.ConceptListResponse;
import com.ripple.BE.learning.service.concept.ConceptService;
import com.ripple.BE.user.domain.CustomUserDetails;
import com.ripple.BE.user.domain.type.Level;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/learning")
@Tag(name = "Concept", description = "개념 학습 API")
public class ConceptController {

    private final ConceptService conceptService;

    @Operation(
            summary = "개념 학습 세트 조회",
            description = "개념 학습 세트를 조회합니다. 개념 학습 세트 id와 레벨을 받아 개념 학습 목록을 반환합니다.")
    @GetMapping("/{learningSetId}/concepts")
    public ResponseEntity<ApiResponse<Object>> getConcepts(
            final @PathVariable("learningSetId") long learningSetId,
            final @RequestParam(defaultValue = "BEGINNER") Level level) {

        ConceptListDTO conceptListDTO = conceptService.getConcepts(learningSetId, level);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(ConceptListResponse.toConceptListResponse(conceptListDTO)));
    }

    @Operation(
            summary = "개념 학습 완료 처리",
            description = "개념 학습을 완료 처리합니다. 개념 학습 세트 id와 레벨을 받아 개념 학습을 완료합니다.")
    @PostMapping("/{learningSetId}/concepts/complete")
    public ResponseEntity<ApiResponse<?>> completeConcept(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("learningSetId") long learningSetId,
            final @RequestParam(defaultValue = "BEGINNER") Level level) {

        conceptService.completeConceptLearning(currentUser.getId(), learningSetId, level);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.EMPTY_RESPONSE);
    }

    @Operation(summary = "개념 학습 스크랩", description = "개념 학습을 스크랩 처리합니다.")
    @PostMapping("/concept/{conceptId}/scrap")
    public ResponseEntity<ApiResponse<?>> scrapConcept(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("conceptId") long conceptId) {

        conceptService.scrapConcept(currentUser.getId(), conceptId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.EMPTY_RESPONSE);
    }

    @Operation(summary = "개념 학습 스크랩 취소", description = "개념 학습 스크랩을 취소합니다.")
    @DeleteMapping("/concept/{conceptId}/scrap")
    public ResponseEntity<ApiResponse<Object>> unscrapConcept(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("conceptId") long id) {

        conceptService.removeScrapFromConcept(id, currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }

    @Operation(
            summary = "개별 개념 학습 조회",
            description = "개별 개념 학습을 조회합니다." + " 개념 id를 받아 개념 상세 정보를 반환합니다. 스크랩한 개념보기 기능을 위한 API입니다.")
    @GetMapping("/concept/{conceptId}")
    public ResponseEntity<ApiResponse<Object>> getConcept(
            final @PathVariable("conceptId") long conceptId) {

        ConceptDTO concept = conceptService.getConcept(conceptId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(ConceptDetailResponse.toConceptDetailResponse(concept)));
    }
}
