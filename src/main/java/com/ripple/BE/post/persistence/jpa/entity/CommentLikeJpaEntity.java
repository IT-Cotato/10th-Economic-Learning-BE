package com.ripple.BE.post.persistence.jpa.entity;

import com.ripple.BE.global.entity.BaseJpaEntity;
import com.ripple.BE.post.domain.comment.CommentLike;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "comment_likes")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLikeJpaEntity extends BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "comment_id")
    private Long commentId;

    @Builder(access = AccessLevel.PRIVATE)
    private CommentLikeJpaEntity(Long id, Long userId, Long commentId) {
        this.id = id;
        this.userId = userId;
        this.commentId = commentId;
    }

    public static CommentLikeJpaEntity from(CommentLike commentLike) {
        return CommentLikeJpaEntity.builder()
                .id(commentLike.getId())
                .userId(commentLike.getUserId())
                .commentId(commentLike.getCommentId())
                .build();
    }

    public CommentLike toModel() {
        return CommentLike.withId(id, userId, commentId);
    }
}
