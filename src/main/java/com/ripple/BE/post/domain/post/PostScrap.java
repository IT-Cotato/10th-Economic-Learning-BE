package com.ripple.BE.post.domain.post;

import com.ripple.BE.post.persistence.jpa.entity.PostScrapJpaEntity;
import com.ripple.BE.user.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostScrap {

    private final Long id;
    private final User user;
    private final Post post;

    public static PostScrap of(User user, Post post) {
        return new PostScrap(null, user, post); // id는 저장 시 부여
    }

    public static PostScrap from(PostScrapJpaEntity postScrapJpaEntity) {
        return new PostScrap(
                postScrapJpaEntity.getId(),
                postScrapJpaEntity.getUser(),
                Post.from(postScrapJpaEntity.getPost()));
    }
}
