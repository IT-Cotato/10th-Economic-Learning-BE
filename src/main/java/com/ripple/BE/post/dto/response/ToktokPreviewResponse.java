package com.ripple.BE.post.dto.response;

import com.ripple.BE.global.utils.RelativeTimeFormatter;
import com.ripple.BE.post.dto.ToktokDTO;
import com.ripple.BE.user.dto.response.UserRandomProfileListResponse;

public record ToktokPreviewResponse(
        Long id,
        String title,
        long participantCount,
        long likeCount,
        String imageUrl,
        Boolean isScraped,
        String createdDate,
        UserRandomProfileListResponse userRandomProfileListResponse) {

    public static ToktokPreviewResponse toToktokPreviewResponse(ToktokDTO toktokDTO) {
        return new ToktokPreviewResponse(
                toktokDTO.id(),
                toktokDTO.title(),
                toktokDTO.commentCount(),
                toktokDTO.likeCount(),
                toktokDTO.imageList().imageDTOList().isEmpty()
                        ? null
                        : toktokDTO.imageList().imageDTOList().get(0).url(),
                toktokDTO.isScraped(),
                RelativeTimeFormatter.formatRelativeTime(toktokDTO.usedDate().atStartOfDay()),
                toktokDTO.userRandomProfileListDTO() == null
                        ? null
                        : UserRandomProfileListResponse.toUserRandomProfileListResponse(
                                toktokDTO.userRandomProfileListDTO()));
    }
}
