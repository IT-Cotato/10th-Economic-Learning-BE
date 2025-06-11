package com.ripple.BE.post.dto.response;

import com.ripple.BE.global.utils.RelativeTimeFormatter;
import com.ripple.BE.image.dto.response.ImageResponse;
import com.ripple.BE.post.domain.post.Post;
import com.ripple.BE.post.domain.type.PostType;
import com.ripple.BE.user.domain.User;
import java.util.List;

public record PostResponseDTO(
        String title,
        String author,
        Long authorId,
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
        List<CommentResponseDTO> commentList,
        String createdDate) {

    public static PostResponseDTO of(
            Post post,
            User author,
            List<ImageResponse> imageList,
            List<CommentResponseDTO> commentList,
            boolean isScraped,
            boolean isLiked,
            boolean isAuthor) {
        return new PostResponseDTO(
                post.getTitle(),
                author.getNickname(),
                post.getAuthorId(),
                author.getProfileImage() == null ? null : author.getProfileImage().getS3Info().getUrl(),
                post.getContent(),
                post.getType(),
                post.getLikeCount(),
                post.getCommentCount(),
                post.getScrapCount(),
                isScraped,
                isLiked,
                isAuthor,
                imageList,
                commentList,
                RelativeTimeFormatter.formatRelativeTime(post.getCreatedDate()));
    }
}
