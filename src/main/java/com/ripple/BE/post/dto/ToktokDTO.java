package com.ripple.BE.post.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ripple.BE.image.dto.ImageListDTO;
import com.ripple.BE.post.domain.Post;
import com.ripple.BE.post.domain.type.PostType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public record ToktokDTO(
        Long id,
        String title,
        String content,
        PostType type,
        Long likeCount,
        Long commentCount,
        Long scrapCount,
        ImageListDTO imageList,
        Boolean isScraped,
        Boolean isLiked,
        @JsonSerialize(using = LocalDateTimeSerializer.class)
                @JsonDeserialize(using = LocalDateTimeDeserializer.class)
                LocalDateTime createdDate,
        @JsonSerialize(using = LocalDateTimeSerializer.class)
                @JsonDeserialize(using = LocalDateTimeDeserializer.class)
                LocalDateTime modifiedDate,
        @JsonSerialize(using = LocalDateSerializer.class)
                @JsonDeserialize(using = LocalDateDeserializer.class)
                LocalDate usedDate,
        CommentListDTO commentListDTO) {

    private static final String TITLE = "title";
    private static final String CONTENT = "content";

    public static ToktokDTO toToktokDTO(final Post post) {
        return new ToktokDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getType(),
                post.getLikeCount(),
                post.getCommentCount(),
                post.getScrapCount(),
                ImageListDTO.toImageListDTO(post.getImageList()),
                post.getIsScrapped(),
                post.getIsLiked(),
                post.getCreatedDate(),
                post.getModifiedDate(),
                post.getUsedDate(),
                null);
    }

    public static ToktokDTO toToktokDTO(final Post post, final CommentListDTO commentListDTO) {
        return new ToktokDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getType(),
                post.getLikeCount(),
                post.getCommentCount(),
                post.getScrapCount(),
                ImageListDTO.toImageListDTO(post.getImageList()),
                post.getIsScrapped(),
                post.getIsLiked(),
                post.getCreatedDate(),
                post.getModifiedDate(),
                post.getUsedDate(),
                commentListDTO);
    }

    public static ToktokDTO toToktokDTO(final Map<String, String> excelData) {
        return new ToktokDTO(
                null,
                excelData.get(TITLE),
                excelData.get(CONTENT),
                PostType.ECONOMY_TALK,
                null,
                null,
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
