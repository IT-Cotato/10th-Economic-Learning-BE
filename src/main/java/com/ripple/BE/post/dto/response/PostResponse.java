package com.ripple.BE.post.dto.response;

import com.ripple.BE.global.utils.RelativeTimeFormatter;
import com.ripple.BE.image.dto.ImageDTO;
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
        List<String> imageList,
        String createdDate,
        CommentListResponse commentListResponse) {

    public static PostResponse toPostResponse(PostDTO postDTO) {
        return new PostResponse(
                postDTO.id(),
                postDTO.title(),
                postDTO.author().nickname(),
                postDTO.author().profileImage().url(),
                postDTO.content(),
                postDTO.type(),
                postDTO.likeCount(),
                postDTO.commentCount(),
                postDTO.scrapCount(),
                postDTO.imageList().imageDTOList().stream().map(ImageDTO::url).toList(),
                RelativeTimeFormatter.formatRelativeTime(postDTO.createdDate()),
                CommentListResponse.toCommentListResponse(postDTO.commentListDTO()));
    }
}
