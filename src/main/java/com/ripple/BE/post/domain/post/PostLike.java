package com.ripple.BE.post.domain.post;

import com.ripple.BE.global.entity.BaseEntity;
import com.ripple.BE.post.persistence.jpa.entity.PostLikeJpaEntity;
import com.ripple.BE.user.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostLike extends BaseEntity {

    private final Long id;
    private final User user;
    private final Post post;

    public static PostLike of(User user, Post post) {
        return new PostLike(null, user, post); // id는 저장소에서 할당
    }

    public static PostLike from(PostLikeJpaEntity postLikeJpaEntity) {
        return new PostLike(
                postLikeJpaEntity.getId(),
                postLikeJpaEntity.getUser(),
                Post.from(postLikeJpaEntity.getPost()));
    }
}
