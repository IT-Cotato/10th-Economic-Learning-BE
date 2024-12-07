package com.ripple.BE.post.domain;

import com.ripple.BE.global.entity.BaseEntity;
import com.ripple.BE.image.domain.Image;
import com.ripple.BE.post.domain.type.PostType;
import com.ripple.BE.user.domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
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
@AllArgsConstructor
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(min = 2, max = 50)
    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author; // 작성자

    @Size(min = 2, max = 500)
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "like_count")
    private long likeCount = 0L; // 좋아요 수

    @Column(name = "comment_count")
    private long commentCount = 0L; // 댓글 수

    @Column(name = "post_type", nullable = false)
    private PostType type; // 게시글 타입

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>(); // 댓글 목록

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostCategory> postCategoryList = new ArrayList<>(); // 게시글 카테고리 목록

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> imageList = new ArrayList<>();
}
