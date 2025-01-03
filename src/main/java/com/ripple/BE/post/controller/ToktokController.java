package com.ripple.BE.post.controller;

import com.ripple.BE.global.dto.response.ApiResponse;
import com.ripple.BE.post.dto.PostDTO;
import com.ripple.BE.post.dto.response.PostResponse;
import com.ripple.BE.post.service.PostService;
import com.ripple.BE.post.service.ToktokService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/post")
@Tag(name = "Toktok", description = "경제톡톡 API")
public class ToktokController {

    private final ToktokService toktokService;
    private final PostService postService;

    @Operation(summary = "오늘의 경제 톡톡 주제 조회", description = "오늘의 경제 톡톡 주제를 조회합니다.")
    @GetMapping("/toktok")
    public ResponseEntity<ApiResponse<Object>> getToktok() {
        PostDTO postDTO = toktokService.getToktok();

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(PostResponse.toPostResponse(postDTO)));
    }
}
