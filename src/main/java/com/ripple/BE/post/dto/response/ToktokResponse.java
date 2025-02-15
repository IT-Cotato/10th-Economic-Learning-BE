package com.ripple.BE.post.dto.response;

import com.ripple.BE.global.utils.RelativeTimeFormatter;
import com.ripple.BE.image.dto.ImageDTO;
import com.ripple.BE.post.dto.ToktokDTO;
import java.util.List;

public record ToktokResponse(
        Long id,
        String title,
        String content,
        long participantCount,
        long likeCount,
        List<String> imageList,
        String createdDate,
        boolean isScraped,
        boolean isLiked,
        CommentListResponse commentListResponse) {

    public static ToktokResponse toToktokResponse(ToktokDTO toktokDTO) {

        return new ToktokResponse(
                toktokDTO.id(),
                toktokDTO.title(),
                toktokDTO.content(),
                toktokDTO.commentCount(),
                toktokDTO.likeCount(),
                toktokDTO.imageList().imageDTOList().stream().map(ImageDTO::url).toList(),
                RelativeTimeFormatter.formatRelativeTime(toktokDTO.usedDate().atStartOfDay()),
                toktokDTO.isScraped(),
                toktokDTO.isLiked(),
                CommentListResponse.toCommentListResponse(toktokDTO.commentListDTO()));
    }
}
