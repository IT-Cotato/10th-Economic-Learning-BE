package com.ripple.BE.post.domain.comment;

import com.ripple.BE.post.domain.post.Post;
import com.ripple.BE.post.exception.PostException;
import com.ripple.BE.post.exception.errorcode.PostErrorCode;
import com.ripple.BE.post.persistence.jpa.entity.CommentJpaEntity;
import com.ripple.BE.user.domain.User;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Comment {

    private final Long id;
    private String content;

    private long likeCount;
    private long replyCount;
    private boolean isDeleted;

    private final User commenter;
    private final Post post;
    private final Comment parent;

    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;

    public static Comment of(String content, User user, Post post) {
        return new Comment(null, content, 0, 0, false, user, post, null, null, null);
    }

    public static Comment of(String content, User user, Post post, Comment parent) {
        return new Comment(null, content, 0, 0, false, user, post, parent, null, null);
    }

    public static Comment from(CommentJpaEntity commentJpaEntity) {
        return new Comment(
                commentJpaEntity.getId(),
                commentJpaEntity.getContent(),
                commentJpaEntity.getLikeCount(),
                commentJpaEntity.getReplyCount(),
                commentJpaEntity.isDeleted(),
                commentJpaEntity.getCommenter(),
                Post.from(commentJpaEntity.getPost()),
                commentJpaEntity.getParent() != null ? Comment.from(commentJpaEntity.getParent()) : null,
                commentJpaEntity.getCreatedDate(),
                commentJpaEntity.getModifiedDate());
    }

    public boolean isRoot() {
        return parent == null;
    }

    public boolean hasNoChildren() {
        return replyCount == 0;
    }

    public void softDeleteAsRoot() {
        this.isDeleted = true;
        this.content = "삭제된 댓글입니다";
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public boolean isOwnedBy(User user) {
        return this.commenter != null && this.commenter.equals(user);
    }

    public void validateReplyable(Post post) {
        if (this.isDeleted || this.parent != null) {
            throw new PostException(PostErrorCode.COMMENT_NOT_FOUND);
        }
        if (!Objects.equals(this.post.getId(), post.getId())) {
            throw new PostException(PostErrorCode.POST_NOT_FOUND);
        }
    }

    public void validateDeletableBy(User user, Post post) {

        if (!isOwnedBy(user) || isDeleted || !Objects.equals(this.post.getId(), post.getId())) {
            throw new PostException(PostErrorCode.COMMENT_NOT_FOUND);
        }
    }

    public void validateUpdatableBy(User user, Post post) {
        if (!isOwnedBy(user) || isDeleted || !Objects.equals(this.post.getId(), post.getId())) {
            throw new PostException(PostErrorCode.COMMENT_NOT_FOUND);
        }
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        this.likeCount--;
    }

    public void increaseReplyCount() {
        this.replyCount++;
    }

    public void decreaseReplyCount() {
        this.replyCount--;
    }
}
