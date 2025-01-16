package com.ripple.BE.post.repository.postscrap;

import static com.ripple.BE.post.domain.QPost.post;
import static com.ripple.BE.post.domain.QPostScrap.postScrap;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ripple.BE.post.domain.Post;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostScrapRepositoryCustomImpl implements PostScrapRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Post> findPostsScrappedByUser(Long userId) {
        return queryFactory
                .select(post)
                .from(postScrap)
                .join(postScrap.post, post)
                .where(postScrap.user.id.eq(userId))
                .fetch();
    }
}
