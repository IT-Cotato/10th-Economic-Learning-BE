package com.ripple.BE.post.dto.response;

import com.ripple.BE.post.dto.ToktokListDTO;
import java.util.List;

public record ToktokPreviewListResponse(List<ToktokPreviewResponse> toktokPreviewResponseList) {

    public static ToktokPreviewListResponse toToktokPreviewListResponse(ToktokListDTO toktokListDTO) {
        return new ToktokPreviewListResponse(
                toktokListDTO.toktokDTOList().stream()
                        .map(ToktokPreviewResponse::toToktokPreviewResponse)
                        .toList());
    }
}
