package com.ripple.BE.post.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ripple.BE.post.domain.Comment;
import com.ripple.BE.user.dto.CommunityUserDTO;
import java.time.LocalDateTime;
import java.util.List;

public record CommentDTO(
        Long id,
        String content,
        long likeCount,
        CommunityUserDTO commenter,
        boolean isDeleted,
        boolean isLiked,
        boolean isAuthor,
        long replyCount,
        List<CommentDTO> children,
        @JsonSerialize(using = LocalDateTimeSerializer.class)
                @JsonDeserialize(using = LocalDateTimeDeserializer.class)
                LocalDateTime createdDate,
        @JsonSerialize(using = LocalDateTimeSerializer.class)
                @JsonDeserialize(using = LocalDateTimeDeserializer.class)
                LocalDateTime modifiedDate) {

    public static CommentDTO toCommentDTO(Comment comment) {
        return new CommentDTO(
                comment.getId(),
                comment.getContent(),
                comment.getLikeCount(),
                CommunityUserDTO.toCommunityUserDTO(comment.getCommenter()),
                comment.isDeleted(),
                comment.getIsLiked(),
                comment.getIsAuthor(),
                comment.getReplyCount(),
                comment.getChildren().stream().map(CommentDTO::toCommentDTO).toList(),
                comment.getCreatedDate(),
                comment.getModifiedDate());
    }
}
