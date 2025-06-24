package com.ripple.BE.user.domain;

import com.ripple.BE.global.entity.BaseJpaEntity;
import com.ripple.BE.image.persistence.jpa.entity.ImageJpaEntity;
import com.ripple.BE.user.domain.type.BusinessType;
import com.ripple.BE.user.domain.type.Gender;
import com.ripple.BE.user.domain.type.Job;
import com.ripple.BE.user.domain.type.Level;
import com.ripple.BE.user.domain.type.LoginType;
import com.ripple.BE.user.domain.type.Role;
import com.ripple.BE.user.dto.request.UpdateUserProfileRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "users")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(min = 5, max = 50)
    @Column(name = "account_email", nullable = false, unique = true)
    private String accountEmail; // 카카오 로그인 시에는 카카오 서버에서 받아옴

    @Size(min = 8, max = 255)
    @Column(name = "password")
    private String password; // 기본 로그인에서만 사용

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Size(min = 2, max = 20)
    @Column(name = "nickname", unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "business_type", length = 50)
    private BusinessType businessType; // 업종

    @Enumerated(EnumType.STRING)
    @Column(name = "job", length = 50)
    private Job job; // 직무

    @Column(name = "birthdate")
    private Date birthDate; // 연령대

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profile_image_id")
    private ImageJpaEntity profileImage; // 프로필 사진

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

    @Setter
    @Column(name = "is_community_alarm_allowed")
    private boolean isCoummunityAlarmAllowed = false; // 커뮤니티 푸시 알람 여부

    @Column(name = "is_profile_completed")
    private boolean isProfileCompleted = false; // 최초 1회 프로필 등록

    @Column(name = "is_level_test_completed")
    private boolean isLevelTestCompleted = false; // 레벨 테스트 완료 여부

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Attendance attendance; // 출석 정보

    @Column(length = 100, unique = true)
    private String keyCode; // 카카오 로그인 시 발급되는 고유 코드

    @Column(name = "quiz_count", nullable = false, columnDefinition = "INT DEFAULT 0")
    private int quizCount;

    @Column(name = "correct_count", nullable = false, columnDefinition = "INT DEFAULT 0")
    private int correctCount;

    @Column(name = "beginner_completed_count", nullable = false, columnDefinition = "INT DEFAULT 0")
    private int beginnerCompletedCount;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(
            name = "intermediate_completed_count",
            nullable = false,
            columnDefinition = "INT DEFAULT 0")
    private int intermediateCompletedCount;

    @Column(name = "advanced_completed_count", nullable = false, columnDefinition = "INT DEFAULT 0")
    private int advancedCompletedCount;

    // 카카오 로그인 시 사용
    @Builder(builderMethodName = "kakaoBuilder", buildMethodName = "buildKakaoUser")
    public User(String keyCode, String accountEmail, LoginType loginType) {
        this.keyCode = keyCode;
        this.accountEmail = accountEmail;
        this.loginType = loginType;
        this.role = Role.USER;
        this.currentLevel = Level.BEGINNER;
        this.isLevelTestCompleted = false;
    }

    // 기본 로그인 시 사용
    @Builder(builderMethodName = "basicBuilder", buildMethodName = "buildBasicUser")
    public User(String accountEmail, String password) {
        this.accountEmail = accountEmail;
        this.password = password;
        this.loginType = LoginType.BASIC;
        this.role = Role.ADMIN;
        this.currentLevel = Level.BEGINNER;
        this.isLevelTestCompleted = false;
    }

    public void updateProfile(UpdateUserProfileRequest request) {
        this.nickname = request.nickname();
        this.businessType = BusinessType.from(request.businessType());
        this.job = Job.from(request.job());
        this.birthDate = request.birthDate();
        this.gender = request.gender();
        this.profileIntro = request.profileIntro();
        this.isLearningAlarmAllowed = request.isLearningAlarmAllowed();
        this.isCoummunityAlarmAllowed = request.isCommunityAlarmAllowed();
        this.isProfileCompleted = true;
    }

    public long getCompletedCountByLevel(Level level) {
        return switch (level) {
            case BEGINNER -> beginnerCompletedCount;
            case INTERMEDIATE -> intermediateCompletedCount;
            case ADVANCED -> advancedCompletedCount;
        };
    }

    public void updateLevel(Level level) {
        this.currentLevel = level;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateBusinessType(String businessType) {
        this.businessType = BusinessType.from(businessType);
    }

    public void updateJob(String job) {
        this.job = Job.from(job);
    }

    public void updateBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void updateGender(Gender gender) {
        this.gender = gender;
    }

    public void updateProfileIntro(String profileIntro) {
        this.profileIntro = profileIntro;
    }

    public void updateProfileImage(ImageJpaEntity image) {
        this.profileImage = image;
    }

    public void updateLearningAlarmAllowed(boolean isLearningAlarmAllowed) {
        this.isLearningAlarmAllowed = isLearningAlarmAllowed;
    }

    public void updateCommunityAlarmAllowed(boolean isCommunityAlarmAllowed) {
        this.isCoummunityAlarmAllowed = isCommunityAlarmAllowed;
    }

    public void updateLevelTestCompleted(boolean isLevelTestCompleted) {
        this.isLevelTestCompleted = isLevelTestCompleted;
    }
}
