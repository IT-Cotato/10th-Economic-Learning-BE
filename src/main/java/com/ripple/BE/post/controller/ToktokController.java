package com.ripple.BE.post.controller;

import com.ripple.BE.global.dto.response.ApiResponse;
import com.ripple.BE.post.domain.type.PostSort;
import com.ripple.BE.post.dto.ToktokDTO;
import com.ripple.BE.post.dto.ToktokListDTO;
import com.ripple.BE.post.dto.response.ToktokPreviewListResponse;
import com.ripple.BE.post.dto.response.ToktokPreviewResponse;
import com.ripple.BE.post.dto.response.ToktokResponse;
import com.ripple.BE.post.service.ToktokAdminService;
import com.ripple.BE.post.service.ToktokService;
import com.ripple.BE.user.domain.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/post")
@Tag(name = "Toktok", description = "경제톡톡 API")
public class ToktokController {

    private final ToktokService toktokService;
    private final ToktokAdminService toktokAdminService;

    @Operation(summary = "오늘의 경제 톡톡 주제 조회", description = "오늘의 경제 톡톡 주제를 조회합니다.")
    @GetMapping("/toktok-today")
    public ResponseEntity<ApiResponse<Object>> getTodayToktok() {
        ToktokDTO toktokDTO = toktokService.getTodayToktok();

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(ToktokPreviewResponse.toToktokPreviewResponse(toktokDTO)));
    }

    @Operation(summary = "경제 톡톡 목록 조회", description = "경제 톡톡 목록을 조회합니다.")
    @GetMapping("/toktok")
    public ResponseEntity<ApiResponse<Object>> getToktoks(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @RequestParam(required = false, defaultValue = "0") @PositiveOrZero int page,
            final @RequestParam(required = false, defaultValue = "RECENT") PostSort sort) {
        ToktokListDTO toktokListDTO = toktokService.getToktoks(page, sort, currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.from(ToktokPreviewListResponse.toToktokPreviewListResponse(toktokListDTO)));
    }

    @Operation(summary = "경제 톡톡 게시물 상세 조회", description = "경제 톡톡 게시물을 상세 조회합니다.")
    @GetMapping("/toktok/{id}")
    public ResponseEntity<ApiResponse<Object>> getToktok(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("id") long id) {

        ToktokDTO toktokDTO = toktokService.getToktok(id, currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(ToktokResponse.toToktokResponse(toktokDTO)));
    }

    @Operation(summary = "경제 톡톡 게시물 생성 (관리자)", description = "경제 톡톡 게시물을 생성합니다. (관리자 전용)")
    @PostMapping("/toktok/excel")
    public ResponseEntity<ApiResponse<Object>> saveToktokByExcel() {
        toktokAdminService.createToktokByExcel();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }
}
