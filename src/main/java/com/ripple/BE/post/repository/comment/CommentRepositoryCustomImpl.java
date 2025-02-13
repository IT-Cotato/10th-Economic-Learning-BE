package com.ripple.BE.post.repository.comment;

import static com.ripple.BE.post.domain.QComment.comment;
import static com.ripple.BE.post.domain.QCommentLike.*;
import static com.ripple.BE.post.domain.QPost.post;
import static com.ripple.BE.user.domain.QUser.*;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ripple.BE.post.domain.Comment;
import com.ripple.BE.post.domain.Post;
import com.ripple.BE.post.domain.QComment;
import com.ripple.BE.user.domain.User;
import java.util.List;
import java.util.stream.Collectors;
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
    public List<Comment> findRootCommentsByPost(Post post, Long userId) {
        List<Tuple> results =
                queryFactory
                        .selectDistinct(comment, commentLike.id)
                        .from(comment)
                        .leftJoin(comment.children)
                        .leftJoin(commentLike)
                        .on(comment.id.eq(commentLike.comment.id).and(commentLike.user.id.eq(userId)))
                        .where(comment.post.eq(post))
                        .orderBy(comment.createdDate.asc())
                        .fetch();

        return results.stream()
                .map(
                        tuple -> {
                            Comment comment = tuple.get(QComment.comment);
                            if (comment != null) {
                                Long likeId = tuple.get(commentLike.id);
                                comment.setIsLiked(likeId != null);
                                comment.setIsAuthor(comment.getCommenter().getId().equals(userId));
                            }
                            return comment;
                        })
                .filter(comment -> comment.getParent() == null)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findUsersByPostId(Long postId) {
        return queryFactory
                .select(user)
                .from(comment)
                .join(comment.commenter, user)
                .where(comment.post.id.eq(postId))
                .distinct()
                .fetch();
    }
}
