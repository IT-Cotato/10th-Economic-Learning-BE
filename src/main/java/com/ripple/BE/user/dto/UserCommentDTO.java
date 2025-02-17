package com.ripple.BE.user.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ripple.BE.post.domain.type.PostType;
import java.time.LocalDateTime;

public record UserCommentDTO(
        Long commentId,
        String comment,
        String postTitle,
        PostType type,
        @JsonSerialize(using = LocalDateTimeSerializer.class)
                @JsonDeserialize(using = LocalDateTimeDeserializer.class)
                LocalDateTime createdDate) {
    public static UserCommentDTO of(
            Long commentId, String comment, String postTitle, PostType type, LocalDateTime createdDate) {
        return new UserCommentDTO(commentId, comment, postTitle, type, createdDate);
    }
}
