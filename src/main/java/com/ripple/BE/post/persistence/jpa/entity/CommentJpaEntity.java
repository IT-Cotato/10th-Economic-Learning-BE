package com.ripple.BE.post.persistence.jpa.entity;

import com.ripple.BE.global.entity.BaseJpaEntity;
import com.ripple.BE.post.domain.comment.Comment;
import com.ripple.BE.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "comments")
@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentJpaEntity extends BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(min = 1, max = 255)
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "like_count")
    private long likeCount = 0L; // 좋아요 수

    @Column(name = "reply_count")
    private long replyCount = 0L; // 답글 수

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostJpaEntity post; // 게시글

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User commenter; // 댓글 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private CommentJpaEntity parent; // 상위 댓글

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false; // 삭제 여부

    public static CommentJpaEntity from(Comment comment) {
        return CommentJpaEntity.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .likeCount(comment.getLikeCount())
                .replyCount(comment.getReplyCount())
                .post(PostJpaEntity.from(comment.getPost()))
                .commenter(comment.getCommenter())
                .parent(comment.getParent() == null ? null : CommentJpaEntity.from(comment.getParent()))
                .isDeleted(comment.isDeleted())
                .build();
    }

    public void updateLikeCount(final long count) {
        this.likeCount = count;
    }

    public void updateReplyCount(final long count) {
        this.replyCount = count;
    }
}
