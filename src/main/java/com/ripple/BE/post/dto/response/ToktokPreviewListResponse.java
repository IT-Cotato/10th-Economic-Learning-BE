package com.ripple.BE.post.dto.response;

import com.ripple.BE.post.dto.PostListDTO;
import java.util.List;

public record ToktokPreviewListResponse(List<ToktokPreviewResponse> toktokPreviewResponseList) {

    public static ToktokPreviewListResponse toToktokPreviewListResponse(PostListDTO postListDTO) {
        return new ToktokPreviewListResponse(
                postListDTO.postDTOList().stream()
                        .map(ToktokPreviewResponse::toToktokPreviewResponse)
                        .toList());
    }
}
