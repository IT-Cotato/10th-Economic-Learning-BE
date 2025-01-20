package com.ripple.BE.image.controller;

import com.ripple.BE.global.dto.response.ApiResponse;
import com.ripple.BE.image.dto.response.ImageIdResponse;
import com.ripple.BE.image.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/image")
@Tag(name = "Image", description = "이미지 API")
public class ImageController {

    private final ImageService imageService;

    @Operation(summary = "게시물 단일 사진 추가", description = "게시물을 등록하기 전 단일 사진을 추가합니다.")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<Object>> createPost(final @RequestParam MultipartFile file) {

        long imageId = imageService.addImageToPost(file);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(ImageIdResponse.toImageIdResponse(imageId)));
    }

    @Operation(summary = "게시물 사진 삭제", description = "게시물에 등록된 사진을 삭제합니다.")
    @DeleteMapping("/{imageId}")
    public ResponseEntity<ApiResponse<Object>> deleteImage(
            final @PathVariable("imageId") long imageId) {

        imageService.deleteImage(imageId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }
}
