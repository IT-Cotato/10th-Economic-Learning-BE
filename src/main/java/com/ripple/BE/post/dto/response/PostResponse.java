package com.ripple.BE.post.dto.response;

import com.ripple.BE.global.utils.RelativeTimeFormatter;
import com.ripple.BE.image.dto.response.ImageResponse;
import com.ripple.BE.post.domain.type.PostType;
import com.ripple.BE.post.dto.PostDTO;
import java.util.List;

public record PostResponse(
        Long id,
        String title,
        String author,
        String authorProfileImage,
        String content,
        PostType type,
        long likeCount,
        long commentCount,
        long scrapCount,
        boolean isScraped,
        boolean isLiked,
        boolean isAuthor,
        List<ImageResponse> imageList,
        String createdDate,
        CommentListResponse commentListResponse) {

    public static PostResponse toPostResponse(PostDTO postDTO) {
        return new PostResponse(
                postDTO.id(),
                postDTO.title(),
                postDTO.author().nickname(),
                postDTO.author().profileImage().getS3Info().getUrl(),
                postDTO.content(),
                postDTO.type(),
                postDTO.likeCount(),
                postDTO.commentCount(),
                postDTO.scrapCount(),
                postDTO.isScraped(),
                postDTO.isLiked(),
                postDTO.isAuthor(),
                postDTO.imageList().imageDTOList().stream().map(ImageResponse::toImageResponse).toList(),
                RelativeTimeFormatter.formatRelativeTime(postDTO.createdDate()),
                CommentListResponse.toCommentListResponse(postDTO.commentListDTO()));
    }
}
