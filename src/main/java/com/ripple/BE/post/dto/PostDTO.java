package com.ripple.BE.post.dto;

import com.ripple.BE.image.dto.ImageListDTO;
import com.ripple.BE.post.domain.Post;
import com.ripple.BE.post.domain.type.PostType;
import com.ripple.BE.post.dto.request.PostRequest;
import com.ripple.BE.user.dto.CommunityUserDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PostDTO(
        Long id,
        String title,
        CommunityUserDTO author,
        String content,
        PostType type,
        Long likeCount,
        Long commentCount,
        Long scrapCount,
        ImageListDTO imageList,
        LocalDateTime createdDate,
        LocalDateTime modifiedDate,
        LocalDate usedDate,
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
                post.getScrapCount(),
                ImageListDTO.toImageListDTO(post.getImageList()),
                post.getCreatedDate(),
                post.getModifiedDate(),
                post.getUsedDate(),
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
                post.getScrapCount(),
                ImageListDTO.toImageListDTO(post.getImageList()),
                post.getCreatedDate(),
                post.getModifiedDate(),
                post.getUsedDate(),
                commentListDTO);
    }

    public static PostDTO toPostDTO(final PostRequest postRequest) {
        return new PostDTO(
                null,
                postRequest.title(),
                null,
                postRequest.content(),
                postRequest.type(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
    }
}
