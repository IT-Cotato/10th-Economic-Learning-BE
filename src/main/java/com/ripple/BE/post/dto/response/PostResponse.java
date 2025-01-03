package com.ripple.BE.post.dto.response;

import com.ripple.BE.image.dto.ImageDTO;
import com.ripple.BE.post.domain.type.PostType;
import com.ripple.BE.post.dto.PostDTO;
import java.time.format.DateTimeFormatter;
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
        List<String> imageList,
        String modifiedDate,
        CommentListResponse commentListResponse) {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); // 년-월-일 시:분

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
                postDTO.imageList().imageDTOList().stream().map(ImageDTO::url).toList(),
                postDTO.modifiedDate().format(FORMATTER),
                CommentListResponse.toCommentListResponse(postDTO.commentListDTO()));
    }
}
