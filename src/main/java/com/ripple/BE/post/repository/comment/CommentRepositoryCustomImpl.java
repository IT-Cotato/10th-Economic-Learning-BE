package com.ripple.BE.post.repository.comment;

import static com.ripple.BE.post.domain.QComment.comment;
import static com.ripple.BE.post.domain.QPost.post;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ripple.BE.post.domain.Comment;
import com.ripple.BE.post.domain.Post;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Post> findPostsCommentedByUser(Long userId) {

        return queryFactory
                .select(post)
                .from(comment)
                .join(comment.post, post)
                .where(comment.commenter.id.eq(userId))
                .fetch();
    }

    @Override
    public List<Comment> findRootCommentsByPost(Post post) {

        return queryFactory
                .selectFrom(comment)
                .where(comment.post.eq(post).and(comment.parent.isNull())) // 대댓글이 아닌 경우 필터링
                .orderBy(comment.createdDate.asc())
                .fetch();
    }
}
