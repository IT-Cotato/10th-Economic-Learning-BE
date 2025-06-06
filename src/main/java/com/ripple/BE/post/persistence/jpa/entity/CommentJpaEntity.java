package com.ripple.BE.post.persistence.jpa.entity;

import com.ripple.BE.global.entity.BaseJpaEntity;
import com.ripple.BE.post.domain.comment.Comment;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "comments")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentJpaEntity extends BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(min = 1, max = 255)
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "like_count")
    private long likeCount;

    @Column(name = "reply_count")
    private long replyCount;

    @Column(name = "post_id")
    private Long postId; // 게시글 ID

    @Column(name = "user_id")
    private Long commenterId; // 댓글 작성자 ID

    @Column(name = "parent_id")
    private Long parentId; // 부모 댓글 ID

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @Builder(access = AccessLevel.PRIVATE)
    private CommentJpaEntity(
            Long id,
            String content,
            long likeCount,
            long replyCount,
            Long postId,
            Long commenterId,
            Long parentId,
            boolean isDeleted) {
        this.id = id;
        this.content = content;
        this.likeCount = likeCount;
        this.replyCount = replyCount;
        this.postId = postId;
        this.commenterId = commenterId;
        this.parentId = parentId;
        this.isDeleted = isDeleted;
    }

    public static CommentJpaEntity from(Comment comment) {
        return CommentJpaEntity.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .likeCount(comment.getLikeCount())
                .replyCount(comment.getReplyCount())
                .postId(comment.getPostId())
                .commenterId(comment.getCommenterId())
                .parentId(comment.getParentCommentId())
                .isDeleted(comment.isDeleted())
                .build();
    }

    public Comment toModel() {
        return Comment.withId(
                this.id,
                this.content,
                this.likeCount,
                this.replyCount,
                this.isDeleted,
                this.commenterId,
                this.postId,
                this.parentId,
                this.getCreatedDate(),
                this.getModifiedDate());
    }
}
