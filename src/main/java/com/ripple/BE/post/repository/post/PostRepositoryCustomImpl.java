package com.ripple.BE.post.repository.post;

import static com.ripple.BE.post.domain.QPost.post;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ripple.BE.post.domain.Post;
import com.ripple.BE.post.domain.type.PostSort;
import com.ripple.BE.post.domain.type.PostType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Post> findByType(PostType type, PostSort postSort, Pageable pageable) {
        BooleanExpression predicate = post.type.eq(type);

        List<Post> posts = getPostsByPageable(pageable, predicate, postSort);

        JPAQuery<Long> countQuery = queryFactory.select(post.count()).from(post).where(predicate);

        return PageableExecutionUtils.getPage(posts, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Post> findNormalPosts(Pageable pageable, PostSort postSort) {

        BooleanExpression predicate = post.type.ne(PostType.ECONOMY_TALK);

        List<Post> posts = getPostsByPageable(pageable, predicate, postSort);

        JPAQuery<Long> countQuery = queryFactory.select(post.count()).from(post).where(predicate);

        return PageableExecutionUtils.getPage(posts, pageable, countQuery::fetchOne);
    }

    @Override
    public List<Post> findUserNormalPosts(Long userId) {

        BooleanExpression predicate =
                post.type.ne(PostType.ECONOMY_TALK).and(post.author.id.eq(userId));

        return queryFactory.selectFrom(post).where(predicate).orderBy(post.createdDate.desc()).fetch();
    }

    @Override
    public List<Post> findNewToktokPosts() {
        BooleanExpression predicate = post.type.eq(PostType.ECONOMY_TALK).and(post.usedDate.isNull());
        return queryFactory.selectFrom(post).where(predicate).fetch();
    }

    @Override
    public Page<Post> findUsedToktokPosts(Pageable pageable, PostSort postSort) {
        BooleanExpression predicate =
                post.type.eq(PostType.ECONOMY_TALK).and(post.usedDate.isNotNull());

        List<Post> posts = getPostsByPageableWithToktok(pageable, predicate, postSort);

        JPAQuery<Long> countQuery = queryFactory.select(post.count()).from(post).where(predicate);

        return PageableExecutionUtils.getPage(posts, pageable, countQuery::fetchOne);
    }

    @Override
    public Optional<Post> findTodayToktokPost(LocalDate today) {
        BooleanExpression predicate = post.type.eq(PostType.ECONOMY_TALK).and(post.usedDate.eq(today));
        Post result = queryFactory.selectFrom(post).where(predicate).fetchOne();
        return Optional.ofNullable(result);
    }

    private List<Post> getPostsByPageable(
            Pageable pageable, BooleanExpression predicate, PostSort postSort) {
        if (postSort == PostSort.POPULAR) {
            return queryFactory
                    .selectFrom(post)
                    .where(predicate)
                    .orderBy(
                            post.likeCount.desc(), // 좋아요 수 내림차순
                            post.createdDate.desc() // 생성일 내림차순
                            )
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        } else {
            return queryFactory
                    .selectFrom(post)
                    .where(predicate)
                    .orderBy(post.createdDate.desc()) // 생성일 내림차순
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        }
    }

    private List<Post> getPostsByPageableWithToktok(
            Pageable pageable, BooleanExpression predicate, PostSort postSort) {
        if (postSort == PostSort.POPULAR) {
            return queryFactory
                    .selectFrom(post)
                    .where(predicate)
                    .orderBy(
                            post.likeCount.desc(), // 좋아요 수 내림차순
                            post.createdDate.desc() // 생성일 내림차순
                            )
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        } else {
            return queryFactory
                    .selectFrom(post)
                    .where(predicate)
                    .orderBy(post.usedDate.desc()) // 사용일 내림차순
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        }
    }
}
