package com.ripple.BE.post.dto;

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
        List<CommentDTO> children,
        LocalDateTime createdDate,
        LocalDateTime modifiedDate) {

    public static CommentDTO toCommentDTO(Comment comment) {
        return new CommentDTO(
                comment.getId(),
                comment.getContent(),
                comment.getLikeCount(),
                CommunityUserDTO.toCommunityUserDTO(comment.getCommenter()),
                comment.isDeleted(),
                comment.getChildren().stream().map(CommentDTO::toCommentDTO).toList(),
                comment.getCreatedDate(),
                comment.getModifiedDate());
    }
}
