package com.ripple.BE.post.repository.commentlike;

import static com.ripple.BE.post.domain.QComment.*;
import static com.ripple.BE.post.domain.QCommentLike.*;
import static com.ripple.BE.post.domain.QPost.*;
import static com.ripple.BE.post.domain.QPostLike.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ripple.BE.post.domain.Comment;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentLikeRepositoryCustomImpl implements CommentLikeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Comment> findCommentsLikedByUser(Long userId) {
        return queryFactory
                .select(comment)
                .from(commentLike)
                .join(commentLike.comment, comment)
                .where(commentLike.user.id.eq(userId))
                .fetch();
    }
}
