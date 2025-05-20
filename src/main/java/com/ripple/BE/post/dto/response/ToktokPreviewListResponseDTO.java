package com.ripple.BE.post.dto.response;

import java.util.List;

public record ToktokPreviewListResponseDTO(
        List<ToktokPreviewResponseDTO> postPreviewList, int totalPage, int currentPage) {

    public static ToktokPreviewListResponseDTO of(
            List<ToktokPreviewResponseDTO> postPreviewList, int totalPage, int currentPage) {
        return new ToktokPreviewListResponseDTO(postPreviewList, totalPage, currentPage);
    }
}
