package com.ripple.BE.learning.persistence.jpa.entity.concept;

import com.ripple.BE.global.entity.BaseJpaEntity;
import com.ripple.BE.learning.domain.concept.Concept;
import com.ripple.BE.user.domain.type.Level;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "concepts")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConceptJpaEntity extends BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "concept_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "level")
    private Level level;

    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "explanation", nullable = false, columnDefinition = "TEXT")
    private String explanation;

    @Column(name = "learning_set_id", nullable = false)
    private Long learningSetId;

    @Column(name = "learning_set_name", nullable = false)
    private String learningSetName;

    @Builder(access = AccessLevel.PRIVATE)
    public ConceptJpaEntity(
            Long id,
            Level level,
            String name,
            String explanation,
            Long learningSetId,
            String learningSetName) {
        this.id = id;
        this.level = level;
        this.name = name;
        this.explanation = explanation;
        this.learningSetId = learningSetId;
        this.learningSetName = learningSetName;
    }

    public static ConceptJpaEntity from(Concept concept) {
        return ConceptJpaEntity.builder()
                .id(concept.getId())
                .level(concept.getLevel())
                .name(concept.getName())
                .explanation(concept.getExplanation())
                .learningSetId(concept.getLearningSetId())
                .learningSetName(concept.getLearningSetName())
                .build();
    }

    public Concept toModel() {
        return Concept.withId(id, level, name, explanation, learningSetId, learningSetName);
    }
}
