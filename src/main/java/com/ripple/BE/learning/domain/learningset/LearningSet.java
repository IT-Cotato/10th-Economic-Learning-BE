package com.ripple.BE.learning.domain.learningset;

import com.ripple.BE.global.entity.BaseEntity;
import com.ripple.BE.learning.domain.concept.Concept;
import com.ripple.BE.learning.domain.quiz.Quiz;
import com.ripple.BE.learning.dto.LearningSetDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    @OneToMany(mappedBy = "learningSet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Concept> concepts = new ArrayList<>();

    @OneToMany(mappedBy = "learningSet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Quiz> quizzes = new ArrayList<>();

    @Builder
    public LearningSet(String name) {
        this.name = name;
    }

    public static LearningSet toLearningSet(final LearningSetDTO learningSetDTO) {
        return LearningSet.builder().name(learningSetDTO.name()).build();
    }
}
