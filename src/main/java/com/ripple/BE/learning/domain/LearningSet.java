package com.ripple.BE.learning.domain;

import com.ripple.BE.global.entity.BaseEntity;
import com.ripple.BE.learning.dto.LearningSetDTO;
import com.ripple.BE.user.domain.type.Level;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "learning_sets")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LearningSet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "learning_set_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "level")
    private Level level;

    @Column(name = "learning_set_num", nullable = false)
    private String learningSetNum; // 학습 세트 번호

    @OneToMany(mappedBy = "learningSet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Concept> concepts = new ArrayList<>();

    @OneToMany(mappedBy = "learningSet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Quiz> quizzes = new ArrayList<>();

    @Builder
    public LearningSet(String name, String description, Level level, String learningSetNum) {
        this.name = name;
        this.description = description;
        this.level = level;
        this.learningSetNum = learningSetNum;
    }

    public static LearningSet toLearningSet(final LearningSetDTO learningSetDTO) {
        return LearningSet.builder()
                .name(learningSetDTO.name())
                .description(learningSetDTO.description())
                .level(learningSetDTO.level())
                .learningSetNum(learningSetDTO.learningSetNum())
                .build();
    }
}
