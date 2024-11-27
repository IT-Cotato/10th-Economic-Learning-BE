package com.ripple.BE.dailyQuest.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import com.ripple.BE.global.entity.BaseEntity;
import com.ripple.BE.member.domain.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "dailyQuests")
@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DailyQuest extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "daily_quest_id")
	private Long id;

	@Column(name = "concept_learning")
	private boolean conceptLearning;	//개념 학습 여부

	@Column(name = "completed_quiz_count")
	@ColumnDefault("0")
	private Long completedQuizCount;	//퀴즈 학습 개수

	@Column(name = "news_reading")
	private Boolean newsReading;	//뉴스 리딩 여부

	@Column(name = "date")
	private LocalDateTime date;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

}
