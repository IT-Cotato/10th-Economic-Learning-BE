package com.ripple.BE.post.persistence.jpa.entity;

import com.ripple.BE.global.entity.BaseJpaEntity;
import com.ripple.BE.post.domain.post.Post;
import com.ripple.BE.post.domain.type.PostType;
import com.ripple.BE.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "posts")
@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    @Column(name = "post_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PostType type;

    @Column(name = "like_count")
    private long likeCount = 0L;

    @Column(name = "comment_count")
    private long commentCount = 0L;

    @Column(name = "scrap_count")
    private long scrapCount = 0L;

    @Column(name = "used_date")
    private LocalDate usedDate; // 사용 날짜

    public static PostJpaEntity from(Post post) {
        return PostJpaEntity.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getAuthor())
                .type(post.getType())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .scrapCount(post.getScrapCount())
                .build();
    }

    public void update(final Post post) {
        this.title = post.getTitle();
        this.content = post.getContent();
        this.type = post.getType();
    }

    public void updateUsedDate(LocalDate usedDate) {
        this.usedDate = usedDate;
    }

    public void updateCommentCount(final long count) {
        this.commentCount = count;
    }

    public void updateLikeCount(final long count) {
        this.likeCount = count;
    }

    public void updateScrapCount(final long count) {
        this.scrapCount = count;
    }
}
