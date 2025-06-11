package com.ripple.BE.post.persistence.jpa.entity;

import com.ripple.BE.global.entity.BaseJpaEntity;
import com.ripple.BE.post.domain.post.PostLike;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "post_likes")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLikeJpaEntity extends BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Builder(access = AccessLevel.PRIVATE)
    private PostLikeJpaEntity(Long id, Long userId, Long postId) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
    }

    public static PostLikeJpaEntity from(PostLike postLike) {
        return PostLikeJpaEntity.builder()
                .id(postLike.getId())
                .userId(postLike.getUserId())
                .postId(postLike.getPostId())
                .build();
    }

    public PostLike toModel() {
        return PostLike.withId(id, userId, postId);
    }
}
