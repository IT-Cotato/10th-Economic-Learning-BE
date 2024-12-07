package com.ripple.BE.user.domain;

import com.ripple.BE.chatbot.domain.ChatSession;
import com.ripple.BE.global.entity.BaseEntity;
import com.ripple.BE.news.domain.NewsScrap;
import com.ripple.BE.post.domain.Comment;
import com.ripple.BE.post.domain.Post;
import com.ripple.BE.post.domain.PostLike;
import com.ripple.BE.post.domain.PostScrap;
import com.ripple.BE.term.domain.TermScrap;
import com.ripple.BE.user.domain.type.AgeRange;
import com.ripple.BE.user.domain.type.BusinessType;
import com.ripple.BE.user.domain.type.Gender;
import com.ripple.BE.user.domain.type.Job;
import com.ripple.BE.user.domain.type.Level;
import com.ripple.BE.user.domain.type.LoginType;
import com.ripple.BE.user.domain.type.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "users")
@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(min = 5, max = 50)
    @Column(name = "login_id", nullable = false)
    private String loginId; // 식별 아이디

    @Size(min = 8, max = 255)
    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role = Role.USER;

    @Size(min = 2, max = 20)
    @Column(name = "nickname")
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "business_type")
    private BusinessType businessType; // 업종

    @Enumerated(EnumType.STRING)
    @Column(name = "job")
    private Job job; // 직무

    @Column(name = "age_range")
    private AgeRange ageRange; // 연령대

    @Column(name = "birthday")
    private LocalDateTime birthday;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "profile_image_url", length = 255)
    private String profileImageUrl; // 프로필 사진 URL

    @Enumerated(EnumType.STRING)
    @Column(name = "login_type", nullable = false)
    private LoginType loginType;

    @Size(min = 1, max = 255)
    @Column(name = "profile_intro")
    private String profileIntro; // 한줄 소개

    @Enumerated(EnumType.STRING)
    @Column(name = "current_level")
    private Level currentLevel; // 현재 학습 단계

    @Column(name = "is_learning_alarm_allowed", nullable = false)
    private boolean isLearningAlarmAllowed; // 학습 푸시 알람 여부

    @Column(name = "is_community_alarm_allowed", nullable = false)
    private boolean isCoummunityAlarmAllowed; // 커뮤니티 푸시 알람 여부

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> postList = new ArrayList<>(); // 작성한 게시글 목록

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostScrap> postScrapList = new ArrayList<>(); // 스크랩한 게시글 목록

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostLike> postLikeList = new ArrayList<>(); // 좋아요한 게시글 목록

    @OneToMany(mappedBy = "commenter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>(); // 작성한 댓글 목록

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NewsScrap> newsScrapList = new ArrayList<>(); // 스크랩한 뉴스 목록

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TermScrap> termScrapList = new ArrayList<>(); // 스크랩한 용어 목록

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatSession> chatSessionList = new ArrayList<>(); // 채팅 세션 목록

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Attendance attendance; // 출석 정보
}
