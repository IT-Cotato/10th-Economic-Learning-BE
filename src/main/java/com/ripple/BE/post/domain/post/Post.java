package com.ripple.BE.post.domain.post;

import com.ripple.BE.post.domain.type.PostType;
import com.ripple.BE.post.persistence.jpa.entity.PostJpaEntity;
import com.ripple.BE.user.domain.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Post {

    private final Long id;
    private String title;
    private String content;
    private final User author;
    private PostType type;

    private long likeCount;
    private long commentCount;
    private long scrapCount;
    private LocalDate usedDate; // 사용 날짜, 톡톡 게시물에만 사용

    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;

    public static Post of(
            String title, String content, User author, PostType type, LocalDate usedDate) {
        return new Post(null, title, content, author, type, 0, 0, 0, usedDate, null, null);
    }

    public static Post from(PostJpaEntity postJpaEntity) {
        return new Post(
                postJpaEntity.getId(),
                postJpaEntity.getTitle(),
                postJpaEntity.getContent(),
                postJpaEntity.getAuthor(),
                postJpaEntity.getType(),
                postJpaEntity.getLikeCount(),
                postJpaEntity.getCommentCount(),
                postJpaEntity.getScrapCount(),
                postJpaEntity.getUsedDate(),
                postJpaEntity.getCreatedDate(),
                postJpaEntity.getModifiedDate());
    }

    public void update(String title, String content, PostType type) {
        if (title != null) this.title = title;
        if (content != null) this.content = content;
        if (type != null) this.type = type;
    }

    public void updateUsedDate(LocalDate usedDate) {
        this.usedDate = usedDate;
    }

    public boolean isOwnedBy(long userId) {
        return author != null && author.getId().equals(userId);
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        this.likeCount--;
    }

    public void increaseCommentCount() {
        this.commentCount++;
    }

    public void decreaseCommentCount() {
        this.commentCount--;
    }

    public void increaseScrapCount() {
        this.scrapCount++;
    }

    public void decreaseScrapCount() {
        this.scrapCount--;
    }
}
