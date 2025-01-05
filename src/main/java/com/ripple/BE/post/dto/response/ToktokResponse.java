package com.ripple.BE.post.dto.response;

import com.ripple.BE.global.utils.RelativeTimeFormatter;
import com.ripple.BE.image.dto.ImageDTO;
import com.ripple.BE.post.dto.PostDTO;
import java.util.List;

public record ToktokResponse(
        Long id,
        String title,
        String content,
        long participantCount,
        List<String> imageList,
        String createdDate,
        CommentListResponse commentListResponse) {

    public static ToktokResponse toToktokResponse(PostDTO postDTO) {

        return new ToktokResponse(
                postDTO.id(),
                postDTO.title(),
                postDTO.content(),
                postDTO.commentCount(),
                postDTO.imageList().imageDTOList().stream().map(ImageDTO::url).toList(),
                RelativeTimeFormatter.formatRelativeTime(postDTO.usedDate().atStartOfDay()),
                CommentListResponse.toCommentListResponse(postDTO.commentListDTO()));
    }
}
