package com.ripple.BE.member.domain;

import java.time.LocalDateTime;

import com.ripple.BE.member.domain.type.AgeRange;
import com.ripple.BE.member.domain.type.BusinessType;
import com.ripple.BE.member.domain.type.Gender;
import com.ripple.BE.member.domain.type.Job;
import com.ripple.BE.member.domain.type.Role;
import com.ripple.BE.global.entity.BaseEntity;

import com.ripple.BE.member.domain.type.Level;
import com.ripple.BE.member.domain.type.LoginType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.validation.constraints.Size;
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

	@Size(min = 5, max = 50)
	@Column(name = "login_id", nullable = false)
	private String loginId;	//식별 아이디

	@Size(min = 8, max = 255)
	@Column(name = "password", nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private Role role;

	@Size(min = 2, max = 20)
	@Column(name = "name", nullable = false)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "business_type", nullable = false)
	private BusinessType businessType;	//업종

	@Enumerated(EnumType.STRING)
	@Column(name = "job", nullable = false)
	private Job job;	//직무

	@Column(name = "age_range")
	private AgeRange ageRange;	//연령대

	@Column(name = "birthday")
	private LocalDateTime birthday;

	@Enumerated(EnumType.STRING)
	@Column(name = "gender")
	private Gender gender;

	@Column(name = "profile_image_url", length = 255)
	private String profileImageUrl;	//프로필 사진 URL

	@Enumerated(EnumType.STRING)
	@Column(name = "login_type", nullable = false)
	private LoginType loginType;

	@Size(min = 1, max = 255)
	@Column(name = "profile_intro")
	private String profileIntro;	//한줄 소개

	@Enumerated(EnumType.STRING)
	@Column(name = "current_level")
	private Level currentLevel;	//현재 학습 단계

	@Column(name = "is_learning_alarm_allowed", nullable = false)
	private boolean isLearningAlarmAllowed;	//학습 푸시 알람 여부

	@Column(name = "is_community_alarm_allowed", nullable = false)
	private boolean isCoummunityAlarmAllowed;		//커뮤니티 푸시 알람 여부

	@Column(name = "finished_learning_sets")
	private Long finishedLearningSets;	//완료한 학습세트 개수

	@Column(name = "finished_quizzes")
	private Long finishedQuizzes;	//완료한 퀴즈 개수


}
