package com.ripple.BE.learning.domain;

import com.ripple.BE.global.entity.BaseEntity;
import com.ripple.BE.member.domain.type.Level;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

@Table(name="concept")
@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Concept extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "concept_id", nullable = false)
	private Long conceptId;

	@Size(max = 255)
	@Column(name = "name", nullable = false)
	private String name;

	@Size(max = 255)
	@Column(name = "explanation", nullable = false)
	private String explanation;

	@Enumerated(EnumType.STRING)
	@Column(name = "level")
	private Level level;

	@Size(max = 255)
	@Column(name = "example")
	private String example;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "learning_set_id")
	private LearningSet learningSet;
}