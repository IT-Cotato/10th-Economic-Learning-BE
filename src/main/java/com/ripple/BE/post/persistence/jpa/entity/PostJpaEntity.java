package com.ripple.BE.post.persistence.jpa.entity;

import com.ripple.BE.global.entity.BaseJpaEntity;
import com.ripple.BE.post.domain.post.Post;
import com.ripple.BE.post.domain.type.PostType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "posts")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostJpaEntity extends BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(min = 2, max = 50)
    @Column(name = "title", nullable = false)
    private String title;

    @Size(min = 2, max = 3500)
    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "user_id")
    private Long authorId; // 작성자 ID

    @Column(name = "post_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PostType type;

    @Column(name = "like_count")
    private long likeCount;

    @Column(name = "comment_count")
    private long commentCount;

    @Column(name = "scrap_count")
    private long scrapCount;

    @Column(name = "used_date")
    private LocalDate usedDate; // 사용 날짜

    @Builder(access = AccessLevel.PRIVATE)
    private PostJpaEntity(
            Long id,
            String title,
            String content,
            Long authorId,
            PostType type,
            long likeCount,
            long commentCount,
            long scrapCount,
            LocalDate usedDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.type = type;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.scrapCount = scrapCount;
        this.usedDate = usedDate;
    }

    public Post toModel() {
        return Post.withId(
                id,
                title,
                content,
                authorId,
                type,
                likeCount,
                commentCount,
                scrapCount,
                usedDate,
                getCreatedDate(),
                getModifiedDate());
    }

    public static PostJpaEntity from(Post post) {
        return PostJpaEntity.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorId(post.getAuthorId())
                .type(post.getType())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .scrapCount(post.getScrapCount())
                .usedDate(post.getUsedDate())
                .build();
    }
}
