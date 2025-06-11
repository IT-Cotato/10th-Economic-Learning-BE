package com.ripple.BE.post.controller;

import com.ripple.BE.global.dto.response.ApiResponse;
import com.ripple.BE.post.application.ToktokAdminUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/post")
@Tag(name = "Toktok", description = "경제톡톡 API")
public class ToktokAdminController {

    private final ToktokAdminUseCase toktokAdminUseCase;

    @Operation(summary = "경제 톡톡 게시물 생성 (관리자)", description = "경제 톡톡 게시물을 생성합니다. (관리자 전용)")
    @PostMapping("/toktok/admin/excel")
    public ResponseEntity<ApiResponse<Object>> saveToktokByExcel() {
        toktokAdminUseCase.createToktokByExcel();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }
}
