package com.ripple.BE.post.dto.response;

import com.ripple.BE.post.dto.PostListDTO;
import java.util.List;

public record PopularPostPreviewListResponse(
        List<PopularPostPreviewResponse> popularPostPreviewList) {

    public static PopularPostPreviewListResponse toPopularPostPreviewListResponse(
            PostListDTO postListDTO) {
        return new PopularPostPreviewListResponse(
                postListDTO.postDTOList().stream()
                        .map(PopularPostPreviewResponse::toPopularPostPreviewResponse)
                        .toList());
    }
}
