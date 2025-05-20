package com.ripple.BE.post.dto.response;

import java.util.List;

public record PostPreviewListResponseDTO(
        List<PostPreviewResponseDTO> postPreviewList, int totalPage, int currentPage) {
    public static PostPreviewListResponseDTO of(
            List<PostPreviewResponseDTO> postPreviewList, int totalPage, int currentPage) {
        return new PostPreviewListResponseDTO(postPreviewList, totalPage, currentPage);
    }
}
