package com.ripple.BE.post.controller;

import com.ripple.BE.global.dto.response.ApiResponse;
import com.ripple.BE.post.domain.type.PostSort;
import com.ripple.BE.post.dto.PostDTO;
import com.ripple.BE.post.dto.PostListDTO;
import com.ripple.BE.post.dto.response.ToktokPreviewListResponse;
import com.ripple.BE.post.dto.response.ToktokPreviewResponse;
import com.ripple.BE.post.dto.response.ToktokResponse;
import com.ripple.BE.post.service.ToktokService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private final ToktokService toktokService;

    @Operation(summary = "오늘의 경제 톡톡 주제 조회", description = "오늘의 경제 톡톡 주제를 조회합니다.")
    @GetMapping("/toktok-today")
    public ResponseEntity<ApiResponse<Object>> getTodayToktok() {
        PostDTO postDTO = toktokService.getTodayToktok();

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(ToktokPreviewResponse.toToktokPreviewResponse(postDTO)));
    }

    @Operation(summary = "경제 톡톡 목록 조회", description = "경제 톡톡 목록을 조회합니다.")
    @GetMapping("/toktok")
    public ResponseEntity<ApiResponse<Object>> getToktoks(
            final @RequestParam(required = false, defaultValue = "0") @PositiveOrZero int page,
            final @RequestParam(required = false, defaultValue = "RECENT") PostSort sort) {
        PostListDTO postListDTO = toktokService.getToktoks(page, sort);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(ToktokPreviewListResponse.toToktokPreviewListResponse(postListDTO)));
    }

    @Operation(summary = "경제 톡톡 게시물 상세 조회", description = "경제 톡톡 게시물을 상세 조회합니다.")
    @GetMapping("/toktok/{id}")
    public ResponseEntity<ApiResponse<Object>> getToktok(final @PathVariable("id") long id) {

        PostDTO postDTO = toktokService.getToktok(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(ToktokResponse.toToktokResponse(postDTO)));
    }
}
