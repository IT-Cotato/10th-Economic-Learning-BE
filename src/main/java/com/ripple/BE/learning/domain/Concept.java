package com.ripple.BE.learning.domain;

import com.ripple.BE.global.entity.BaseEntity;
import com.ripple.BE.learning.dto.ConceptDTO;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "concepts")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Concept extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "concept_id")
    private Long conceptId;

    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 255)
    @Column(name = "explanation", nullable = false)
    private String explanation;

    @Size(max = 255)
    @Column(name = "example")
    private String example;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "learning_set_id")
    private LearningSet learningSet;

    @Builder
    public Concept(String name, String explanation, String example) {
        this.name = name;
        this.explanation = explanation;
        this.example = example;
    }

    public static Concept toConcept(final ConceptDTO conceptDTO) {

        return Concept.builder()
                .name(conceptDTO.name())
                .explanation(conceptDTO.explanation())
                .example(conceptDTO.example())
                .build();
    }

    public void setLearningSet(LearningSet learningSet) {
        this.learningSet = learningSet;
        learningSet.getConcepts().add(this);
    }
}
