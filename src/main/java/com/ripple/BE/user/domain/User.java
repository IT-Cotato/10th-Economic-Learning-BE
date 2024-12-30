package com.ripple.BE.user.domain;

import com.ripple.BE.chatbot.domain.ChatSession;
import com.ripple.BE.global.entity.BaseEntity;
import com.ripple.BE.learning.domain.LearningSetComplete;
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
import com.ripple.BE.user.dto.UpdateUserProfileRequest;
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
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "users")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(min = 5, max = 50)
    @Column(name = "account_email", nullable = false)
    private String accountEmail; // 카카오 로그인 시에는 카카오 서버에서 받아옴

    @Size(min = 8, max = 255)
    @Column(name = "password")
    private String password; // 기본 로그인에서만 사용

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Size(min = 2, max = 20)
    @Column(name = "nickname")
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "business_type")
    private BusinessType businessType; // 업종

    @Enumerated(EnumType.STRING)
    @Column(name = "job")
    private Job job; // 직무

    @Enumerated(EnumType.STRING)
    @Column(name = "age_range")
    private AgeRange ageRange; // 연령대

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

    @Column(name = "is_learning_alarm_allowed")
    private boolean isLearningAlarmAllowed = false; // 학습 푸시 알람 여부

    @Column(name = "is_community_alarm_allowed")
    private boolean isCoummunityAlarmAllowed = false; // 커뮤니티 푸시 알람 여부

    @Column(name = "is_profile_completed")
    private boolean isProfileCompleted = false; // 최초 1회 프로필 등록

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LearningSetComplete> learningSetCompleteList = new ArrayList<>(); // 학습 완료 목록

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Attendance attendance; // 출석 정보

    @Column(length = 100, unique = true, nullable = true)
    private String keyCode; // 카카오 로그인 시 발급되는 고유 코드

    // 카카오 로그인 시 사용
    @Builder(builderMethodName = "kakaoBuilder", buildMethodName = "buildKakaoUser")
    public User(String keyCode, String accountEmail, String profileImageUrl, LoginType loginType) {
        this.keyCode = keyCode;
        this.accountEmail = accountEmail;
        this.profileImageUrl = profileImageUrl;
        this.loginType = loginType;
        this.role = Role.USER;
    }

    // 기본 로그인 시 사용
    @Builder(builderMethodName = "basicBuilder", buildMethodName = "buildBasicUser")
    public User(String accountEmail, String password) {
        this.accountEmail = accountEmail;
        this.password = password;
        this.loginType = LoginType.BASIC;
        this.role = Role.ADMIN;
    }

    public void updateProfile(UpdateUserProfileRequest request) {
        this.nickname = request.nickname();
        this.businessType = request.businessType();
        this.job = request.job();
        this.ageRange = request.ageRange();
        this.gender = request.gender();
        this.profileIntro = request.profileIntro();
        this.isLearningAlarmAllowed = request.isLearningAlarmAllowed();
        this.isCoummunityAlarmAllowed = request.isCommunityAlarmAllowed();
        this.isProfileCompleted = true;
    }

    public void updateLevel(Level level) {
        this.currentLevel = level;
    }
}
