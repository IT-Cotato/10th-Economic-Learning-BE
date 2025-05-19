package com.ripple.BE.post.controller;

import com.ripple.BE.global.dto.response.ApiResponse;
import com.ripple.BE.post.domain.type.PostSort;
import com.ripple.BE.post.dto.response.ToktokPreviewListResponseDTO;
import com.ripple.BE.post.dto.response.ToktokPreviewResponseDTO;
import com.ripple.BE.post.dto.response.ToktokResponseDTO;
import com.ripple.BE.post.service.ToktokQueryUseCase;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/post")
@Tag(name = "Toktok", description = "경제톡톡 API")
public class ToktokController {

    private final ToktokQueryUseCase toktokUseCase;

    @Operation(summary = "오늘의 경제 톡톡 주제 조회", description = "오늘의 경제 톡톡 주제를 조회합니다. 커뮤니티 홈 화면에 표시됩니다.")
    @GetMapping("/toktok-today")
    public ResponseEntity<ApiResponse<Object>> getTodayToktok(
            final @AuthenticationPrincipal CustomUserDetails currentUser) {

        ToktokPreviewResponseDTO toktokPreviewResponseDTO =
                toktokUseCase.getTodayToktok(currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(toktokPreviewResponseDTO));
    }

    @Operation(
            summary = "경제 톡톡 목록 조회",
            description = "경제 톡톡 목록을 조회합니다. 페이지 번호는 0부터 시작하며, 페이지 당 10개의 경제 톡톡을 반환합니다.")
    @GetMapping("/toktok")
    public ResponseEntity<ApiResponse<Object>> getToktoks(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @RequestParam(required = false, defaultValue = "0") @PositiveOrZero int page,
            final @RequestParam(required = false, defaultValue = "RECENT") PostSort sort) {
        ToktokPreviewListResponseDTO toktokListDTO =
                toktokUseCase.getToktoks(page, sort, currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(toktokListDTO));
    }

    @Operation(summary = "경제 톡톡 게시물 상세 조회", description = "경제 톡톡 게시물을 상세 조회합니다.")
    @GetMapping("/toktok/{id}")
    public ResponseEntity<ApiResponse<Object>> getToktok(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("id") long id) {

        ToktokResponseDTO toktokResponseDTO = toktokUseCase.getToktok(id, currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(toktokResponseDTO));
    }
}
