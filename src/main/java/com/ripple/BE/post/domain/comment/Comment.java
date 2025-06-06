package com.ripple.BE.post.domain.comment;

import com.ripple.BE.post.exception.PostException;
import com.ripple.BE.post.exception.errorcode.PostErrorCode;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Comment {

    private final Long id;
    private final String content;

    private final long likeCount;
    private final long replyCount;
    private final boolean isDeleted;

    private final Long commenterId;
    private final Long postId;
    private final Long parentCommentId;

    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;

    @Builder(access = AccessLevel.PRIVATE)
    private Comment(
            Long id,
            String content,
            long likeCount,
            long replyCount,
            boolean isDeleted,
            Long commenterId,
            Long postId,
            Long parentCommentId,
            LocalDateTime createdDate,
            LocalDateTime modifiedDate) {
        this.id = id;
        this.content = content;
        this.likeCount = likeCount;
        this.replyCount = replyCount;
        this.isDeleted = isDeleted;
        this.commenterId = commenterId;
        this.postId = postId;
        this.parentCommentId = parentCommentId;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static Comment withId(
            Long id,
            String content,
            long likeCount,
            long replyCount,
            boolean deleted,
            Long commenterId,
            Long postId,
            Long parentCommentId,
            LocalDateTime createdDate,
            LocalDateTime modifiedDate) {
        return Comment.builder()
                .id(id)
                .content(content)
                .likeCount(likeCount)
                .replyCount(replyCount)
                .isDeleted(deleted)
                .commenterId(commenterId)
                .postId(postId)
                .parentCommentId(parentCommentId)
                .createdDate(createdDate)
                .modifiedDate(modifiedDate)
                .build();
    }

    public static Comment withoutId(
            String content, Long commenterId, Long postId, Long parentCommentId) {
        return Comment.builder()
                .content(content)
                .likeCount(0)
                .replyCount(0)
                .isDeleted(false)
                .commenterId(commenterId)
                .postId(postId)
                .parentCommentId(parentCommentId)
                .build();
    }

    public boolean isRoot() {
        return parentCommentId == null;
    }

    public boolean hasNoChildren() {
        return replyCount == 0;
    }

    public boolean isOwnedBy(Long userId) {
        return this.commenterId != null && this.commenterId.equals(userId);
    }

    public void validateReplyable(Long postId) {
        if (this.isDeleted || parentCommentId != null) {
            throw new PostException(PostErrorCode.COMMENT_NOT_FOUND);
        }
        if (!Objects.equals(this.postId, postId)) {
            throw new PostException(PostErrorCode.POST_NOT_FOUND);
        }
    }

    public void validateUpdatableBy(Long userId, Long postId) {
        if (!isOwnedBy(userId) || isDeleted || !Objects.equals(this.postId, postId)) {
            throw new PostException(PostErrorCode.COMMENT_NOT_FOUND);
        }
    }

    public void validateDeletableBy(Long userId, Long postId) {
        if (!isOwnedBy(userId) || isDeleted || !Objects.equals(this.postId, postId)) {
            throw new PostException(PostErrorCode.COMMENT_NOT_FOUND);
        }
    }
}
