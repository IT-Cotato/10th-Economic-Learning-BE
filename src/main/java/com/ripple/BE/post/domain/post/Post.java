package com.ripple.BE.post.domain.post;

import com.ripple.BE.post.domain.type.PostType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Post {

    private final Long id;
    private final String title;
    private final String content;
    private final Long authorId;
    private final PostType type;

    private final long likeCount;
    private final long commentCount;
    private final long scrapCount;
    private final LocalDate usedDate; // 사용 날짜, 톡톡 게시물에만 사용

    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;

    @Builder(access = AccessLevel.PRIVATE)
    private Post(
            Long id,
            String title,
            String content,
            Long authorId,
            PostType type,
            long likeCount,
            long commentCount,
            long scrapCount,
            LocalDate usedDate,
            LocalDateTime createdDate,
            LocalDateTime modifiedDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.type = type;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.scrapCount = scrapCount;
        this.usedDate = usedDate;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static Post withId(
            Long id,
            String title,
            String content,
            Long authorId,
            PostType type,
            long likeCount,
            long commentCount,
            long scrapCount,
            LocalDate usedDate,
            LocalDateTime createdDate,
            LocalDateTime modifiedDate) {
        return Post.builder()
                .id(id)
                .title(title)
                .content(content)
                .authorId(authorId)
                .type(type)
                .likeCount(likeCount)
                .commentCount(commentCount)
                .scrapCount(scrapCount)
                .usedDate(usedDate)
                .createdDate(createdDate)
                .modifiedDate(modifiedDate)
                .build();
    }

    public static Post withoutId(
            String title, String content, Long authorId, PostType type, LocalDate usedDate) {
        return Post.builder()
                .title(title)
                .content(content)
                .authorId(authorId)
                .type(type)
                .likeCount(0)
                .commentCount(0)
                .scrapCount(0)
                .usedDate(usedDate)
                .build();
    }

    public boolean isOwnedBy(Long userId) {
        return this.authorId != null && this.authorId.equals(userId);
    }

    public Post increaseLikeCount() {
        return Post.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .authorId(this.authorId)
                .type(this.type)
                .likeCount(this.likeCount + 1)
                .commentCount(this.commentCount)
                .scrapCount(this.scrapCount)
                .usedDate(this.usedDate)
                .createdDate(this.createdDate)
                .modifiedDate(LocalDateTime.now())
                .build();
    }

    public Post decreaseLikeCount() {
        return Post.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .authorId(this.authorId)
                .type(this.type)
                .likeCount(Math.max(0, this.likeCount - 1))
                .commentCount(this.commentCount)
                .scrapCount(this.scrapCount)
                .usedDate(this.usedDate)
                .createdDate(this.createdDate)
                .modifiedDate(LocalDateTime.now())
                .build();
    }

    public Post increaseCommentCount() {
        return Post.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .authorId(this.authorId)
                .type(this.type)
                .likeCount(this.likeCount)
                .commentCount(this.commentCount + 1)
                .scrapCount(this.scrapCount)
                .usedDate(this.usedDate)
                .createdDate(this.createdDate)
                .modifiedDate(LocalDateTime.now())
                .build();
    }

    public Post decreaseCommentCount() {
        return Post.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .authorId(this.authorId)
                .type(this.type)
                .likeCount(this.likeCount)
                .commentCount(Math.max(0, this.commentCount - 1))
                .scrapCount(this.scrapCount)
                .usedDate(this.usedDate)
                .createdDate(this.createdDate)
                .modifiedDate(LocalDateTime.now())
                .build();
    }

    public Post updateUsedDate(LocalDate usedDate) {
        return Post.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .authorId(this.authorId)
                .type(this.type)
                .likeCount(this.likeCount)
                .commentCount(this.commentCount)
                .scrapCount(this.scrapCount)
                .usedDate(usedDate)
                .createdDate(this.createdDate)
                .modifiedDate(LocalDateTime.now())
                .build();
    }

    public Post increaseScrapCount() {
        return Post.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .authorId(this.authorId)
                .type(this.type)
                .likeCount(this.likeCount)
                .commentCount(this.commentCount)
                .scrapCount(this.scrapCount + 1)
                .usedDate(this.usedDate)
                .createdDate(this.createdDate)
                .modifiedDate(LocalDateTime.now())
                .build();
    }

    public Post decreaseScrapCount() {
        return Post.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .authorId(this.authorId)
                .type(this.type)
                .likeCount(this.likeCount)
                .commentCount(this.commentCount)
                .scrapCount(Math.max(0, this.scrapCount - 1))
                .usedDate(this.usedDate)
                .createdDate(this.createdDate)
                .modifiedDate(LocalDateTime.now())
                .build();
    }

    public Post update(String newTitle, String newContent, PostType newType) {
        return Post.builder()
                .id(this.id)
                .title(newTitle)
                .content(newContent)
                .authorId(this.authorId)
                .type(newType)
                .likeCount(this.likeCount)
                .commentCount(this.commentCount)
                .scrapCount(this.scrapCount)
                .usedDate(this.usedDate)
                .createdDate(this.createdDate)
                .modifiedDate(LocalDateTime.now())
                .build();
    }
}
