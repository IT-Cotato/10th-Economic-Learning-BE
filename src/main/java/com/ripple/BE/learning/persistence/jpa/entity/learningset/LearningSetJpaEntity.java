package com.ripple.BE.learning.persistence.jpa.entity.learningset;

import com.ripple.BE.global.entity.BaseJpaEntity;
import com.ripple.BE.learning.domain.learningset.LearningSet;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "learning_sets")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LearningSetJpaEntity extends BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "learning_set_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Builder(access = AccessLevel.PRIVATE)
    public LearningSetJpaEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public LearningSet toModel() {
        return LearningSet.withId(this.id, this.name);
    }

    public static LearningSetJpaEntity from(LearningSet learningSet) {
        return LearningSetJpaEntity.builder()
                .id(learningSet.getId())
                .name(learningSet.getName())
                .build();
    }
}
