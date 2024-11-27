package com.ripple.BE.member.domain;

import com.ripple.BE.global.entity.BaseEntity;
import com.ripple.BE.member.domain.type.Category;
import com.ripple.BE.member.domain.type.Level;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name="member_progress")
@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberProgress extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="member_progress_id")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "category")
	private Category category;

	@Enumerated(EnumType.STRING)
	@Column(name = "level")
	private Level level;

	@Column(name = "progress")
	private Long progress;

	@OneToOne
	@JoinColumn(name="member_id", nullable = false)
	private Member member;
}
