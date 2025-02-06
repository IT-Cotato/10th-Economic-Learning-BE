package com.ripple.BE.post.repository.post;

import static com.ripple.BE.post.domain.QPost.post;
import static com.ripple.BE.post.domain.QPostScrap.*;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ripple.BE.post.domain.Post;
import com.ripple.BE.post.domain.QPost;
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
    public Page<Post> findByType(PostType type, PostSort postSort, Pageable pageable, long userId) {
        BooleanExpression predicate = post.type.eq(type);

        List<Post> posts = getPostsWithScrapByPageable(pageable, predicate, postSort, userId);

        JPAQuery<Long> countQuery = queryFactory.select(post.count()).from(post).where(predicate);

        return PageableExecutionUtils.getPage(posts, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Post> findNormalPosts(Pageable pageable, PostSort postSort, long userId) {

        BooleanExpression predicate = post.type.ne(PostType.ECONOMY_TALK);

        List<Post> posts = getPostsWithScrapByPageable(pageable, predicate, postSort, userId);

        JPAQuery<Long> countQuery = queryFactory.select(post.count()).from(post).where(predicate);

        return PageableExecutionUtils.getPage(posts, pageable, countQuery::fetchOne);
    }

    // 좋아요 수가 10개 이상, 10개 이상인 게시글 중에서 최신순으로 10개 조회
    @Override
    public List<Post> findPopularPosts() {
        int likeCount = 10;
        int limit = 10;

        return queryFactory
                .selectFrom(post)
                .where(post.likeCount.goe(likeCount))
                .orderBy(post.createdDate.desc())
                .limit(limit)
                .fetch();
    }

    @Override
    public Page<Post> searchNormalPosts(String keyword, Pageable pageable, long userId) {
        BooleanExpression predicate = post.type.ne(PostType.ECONOMY_TALK); // 기본 조건

        if (keyword != null && !keyword.trim().isEmpty()) {
            predicate = predicate.and(post.title.contains(keyword).or(post.content.contains(keyword)));
        }

        List<Post> posts = getPostsWithScrapByPageable(pageable, predicate, PostSort.RECENT, userId);

        JPAQuery<Long> countQuery = queryFactory.select(post.count()).from(post).where(predicate);

        return PageableExecutionUtils.getPage(posts, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Post> searchUsedToktokPosts(String keyword, Pageable pageable, long userId) {

        BooleanExpression predicate =
                post.type.eq(PostType.ECONOMY_TALK).and(post.usedDate.isNotNull()); // 기본 조건

        if (keyword != null && !keyword.trim().isEmpty()) {
            predicate = predicate.and(post.title.contains(keyword).or(post.content.contains(keyword)));
        }

        List<Post> posts = getToktoksWithScrapByPageable(pageable, predicate, PostSort.RECENT, userId);

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
    public Page<Post> findUsedToktokPosts(Pageable pageable, PostSort postSort, long userId) {
        BooleanExpression predicate =
                post.type.eq(PostType.ECONOMY_TALK).and(post.usedDate.isNotNull());

        List<Post> posts = getToktoksWithScrapByPageable(pageable, predicate, postSort, userId);

        JPAQuery<Long> countQuery = queryFactory.select(post.count()).from(post).where(predicate);

        return PageableExecutionUtils.getPage(posts, pageable, countQuery::fetchOne);
    }

    @Override
    public Optional<Post> findTodayToktokPost(LocalDate today) {
        BooleanExpression predicate = post.type.eq(PostType.ECONOMY_TALK).and(post.usedDate.eq(today));
        Post result = queryFactory.selectFrom(post).where(predicate).fetchOne();
        return Optional.ofNullable(result);
    }

    private List<Post> getPostsWithScrapByPageable(
            Pageable pageable, BooleanExpression predicate, PostSort postSort, long userId) {

        return getPostsOrToktoksWithScrapByPageable(pageable, predicate, postSort, userId, false);
    }

    private List<Post> getToktoksWithScrapByPageable(
            Pageable pageable, BooleanExpression predicate, PostSort postSort, long userId) {

        return getPostsOrToktoksWithScrapByPageable(pageable, predicate, postSort, userId, true);
    }

    private List<Post> getPostsOrToktoksWithScrapByPageable(
            Pageable pageable,
            BooleanExpression predicate,
            PostSort postSort,
            long userId,
            boolean isToktok) {

        // 동적으로 정렬 조건 설정
        var orderBy =
                (postSort == PostSort.POPULAR)
                        ? new com.querydsl.core.types.OrderSpecifier[] {
                            post.likeCount.desc(), post.createdDate.desc()
                        }
                        : new com.querydsl.core.types.OrderSpecifier[] {
                            isToktok ? post.usedDate.desc() : post.createdDate.desc()
                        };

        List<Tuple> results =
                queryFactory
                        .select(post, postScrap.id) // post와 postScrap.id를 함께 조회
                        .from(post)
                        .leftJoin(postScrap)
                        .on(post.id.eq(postScrap.post.id).and(postScrap.user.id.eq(userId)))
                        .where(predicate)
                        .orderBy(orderBy)
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        return results.stream()
                .map(
                        tuple -> {
                            Post post = tuple.get(QPost.post);
                            Long scrapId = tuple.get(postScrap.id);
                            post.setIsScrapped(scrapId != null);
                            return post;
                        })
                .toList();
    }
}
