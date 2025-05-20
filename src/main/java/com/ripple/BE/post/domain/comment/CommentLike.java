package com.ripple.BE.post.domain.comment;

import com.ripple.BE.post.persistence.jpa.entity.CommentLikeJpaEntity;
import com.ripple.BE.user.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentLike {

    private final Long id;
    private final User user;
    private final Comment comment;

    public static CommentLike of(User user, Comment comment) {
        return new CommentLike(null, user, comment);
    }

    public static CommentLike from(CommentLikeJpaEntity commentLikeJpaEntity) {
        return new CommentLike(
                commentLikeJpaEntity.getId(),
                commentLikeJpaEntity.getUser(),
                Comment.from(commentLikeJpaEntity.getComment()));
    }
}
