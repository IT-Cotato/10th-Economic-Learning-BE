package com.ripple.BE.post.dto;

import com.ripple.BE.image.dto.ImageListDTO;
import com.ripple.BE.post.domain.Post;
import com.ripple.BE.post.domain.type.PostType;
import com.ripple.BE.user.dto.CommunityUserDTO;
import java.time.LocalDateTime;

public record PostDTO(
        Long id,
        String title,
        CommunityUserDTO author,
        String content,
        PostType type,
        long likeCount,
        long commentCount,
        ImageListDTO imageList,
        LocalDateTime modifiedDate,
        CommentListDTO commentListDTO) {

    public static PostDTO toPostDTO(final Post post) {
        return new PostDTO(
                post.getId(),
                post.getTitle(),
                CommunityUserDTO.toCommunityUserDTO(post.getAuthor()),
                post.getContent(),
                post.getType(),
                post.getLikeCount(),
                post.getCommentCount(),
                ImageListDTO.toImageListDTO(post.getImageList()),
                post.getModifiedDate(),
                CommentListDTO.toCommentListDTO(post.getCommentList()));
    }

    public static PostDTO toPostDTO(final Post post, final CommentListDTO commentListDTO) {
        return new PostDTO(
                post.getId(),
                post.getTitle(),
                CommunityUserDTO.toCommunityUserDTO(post.getAuthor()),
                post.getContent(),
                post.getType(),
                post.getLikeCount(),
                post.getCommentCount(),
                ImageListDTO.toImageListDTO(post.getImageList()),
                post.getModifiedDate(),
                commentListDTO);
    }
}
