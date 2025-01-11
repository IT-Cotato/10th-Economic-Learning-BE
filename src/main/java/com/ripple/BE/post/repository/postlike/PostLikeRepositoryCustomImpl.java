package com.ripple.BE.post.repository.postlike;

import static com.ripple.BE.post.domain.QPost.post;
import static com.ripple.BE.post.domain.QPostLike.postLike;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ripple.BE.post.domain.Post;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostLikeRepositoryCustomImpl implements PostLikeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Post> findPostsLikedByUser(Long userId) {

        return queryFactory
                .select(post)
                .from(postLike)
                .join(postLike.post, post)
                .where(postLike.user.id.eq(userId))
                .fetch();
    }
}
