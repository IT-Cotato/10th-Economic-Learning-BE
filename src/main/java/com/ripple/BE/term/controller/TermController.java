package com.ripple.BE.term.controller;

import static com.ripple.BE.global.exception.errorcode.GlobalErrorCode.*;

import com.ripple.BE.global.dto.response.ApiResponse;
import com.ripple.BE.term.dto.TermDTO;
import com.ripple.BE.term.dto.TermListDTO;
import com.ripple.BE.term.dto.response.TermListResponse;
import com.ripple.BE.term.dto.response.TermResponse;
import com.ripple.BE.term.exception.TermException;
import com.ripple.BE.term.service.TermAdminService;
import com.ripple.BE.term.service.TermService;
import com.ripple.BE.user.domain.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.PositiveOrZero;
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
@RequestMapping("/api/v1/terms")
@Tag(name = "Term", description = "용어집 API")
public class TermController {

    private final TermService termService;
    private final TermAdminService termAdminService;

    @Operation(summary = "자음 별 용어 조회", description = "자음 별 용어를 조회합니다.")
    @GetMapping("/search/consonant")
    public ResponseEntity<ApiResponse<Object>> getTermsByInitial(
            final @RequestParam(required = false, defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(value = "consonant", required = false, defaultValue = "ㄱ")
                    final String consonant) {

        if (consonant.length() != 1) {
            throw new TermException(INVALID_PARAMETER);
        }

        TermListDTO termListDTO = termService.getTermsByInitial(page, consonant);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(TermListResponse.toTermListResponse(termListDTO)));
    }

    @Operation(summary = "키워드 별 용어 조회", description = "키워드 별 용어를 조회합니다.")
    @GetMapping("/search/keyword")
    public ResponseEntity<ApiResponse<Object>> getTermsByKeyword(
            final @RequestParam(required = false, defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(value = "keyword", required = false) final String keyword) {

        TermListDTO termListDTO = termService.getTermsByKeyword(page, keyword);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(TermListResponse.toTermListResponse(termListDTO)));
    }

    @Operation(summary = "용어 상세 조회", description = "용어 상세를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> getTerm(@PathVariable final Long id) {

        TermDTO termDTO = termService.getTerm(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(TermResponse.toTermResponse(termDTO)));
    }

    @Operation(summary = "용어 스크랩", description = "용어를 스크랩합니다.")
    @PostMapping("/{id}/scrap")
    public ResponseEntity<ApiResponse<Object>> scrapTerm(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("id") long id) {

        termService.addScrapToTerm(id, currentUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }

    @Operation(summary = "용어 스크랩 취소", description = "용어 스크랩을 취소합니다.")
    @DeleteMapping("/{id}/scrap")
    public ResponseEntity<ApiResponse<Object>> unscrapTerm(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("id") long id) {

        termService.removeScrapFromTerm(id, currentUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }

    @Operation(summary = "용어 생성 (관리자)", description = "용어를 생성합니다.")
    @PostMapping("/excel")
    public ResponseEntity<ApiResponse<?>> saveTermsByExcel() {
        termAdminService.createTermByExcel();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.EMPTY_RESPONSE);
    }
}
