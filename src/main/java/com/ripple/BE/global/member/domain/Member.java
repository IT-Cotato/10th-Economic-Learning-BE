package com.ripple.BE.global.member.domain;

import java.time.LocalDateTime;

import com.ripple.BE.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name="members")
@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", nullable = false)
	private Long id;

	@Column(name = "login_id", nullable = false)
	private String loginId;	//식별 아이디

	@Column(name = "password", nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private Role role;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "occupation")
	private String occupation;	//직업

	@Column(name = "age_range")
	private Long ageRange;	//연령대

	@Column(name = "birthday")
	private LocalDateTime birthday;

	@Enumerated(EnumType.STRING)
	@Column(name = "gender")
	private Gender gender;

	@Column(name = "profile_image_url")
	private String profileImageUrl;	//프로필 사진 URL

	@Enumerated(EnumType.STRING)
	@Column(name = "login_type", nullable = false)
	private LoginType loginType;

	@Column(name = "profile_intro")
	private String profileIntro;	//한줄 소개

	@Enumerated(EnumType.STRING)
	@Column(name = "current_level")
	private Level currentLevel;	//현재 학습 단계

	@Column(name = "is_learning_alarm_allowed", nullable = false)
	private Boolean isLearningAlarmAllowed;	//학습 푸시 알람 여부

	@Column(name = "is_community_alarm_allowed", nullable = false)
	private Boolean isCoummunityAlarmAllowed;		//커뮤니티 푸시 알람 여부

	@Column(name = "finished_learning_sets")
	private Long finishedLearningSets;	//완료한 학습세트 개수

	@Column(name = "finished_quizzes")
	private Long finishedQuizzes;	//완료한 퀴즈 개수


}
