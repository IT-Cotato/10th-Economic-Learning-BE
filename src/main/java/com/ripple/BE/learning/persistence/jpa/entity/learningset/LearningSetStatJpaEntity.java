package com.ripple.BE.learning.persistence.jpa.entity.learningset;

import com.ripple.BE.learning.domain.learningset.LearningSetStat;
import com.ripple.BE.user.domain.type.Level;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "learning_set_stats")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LearningSetStatJpaEntity {

    @Id
    @Enumerated(EnumType.STRING)
    private Level level;

    @Column(name = "quiz_count")
    private int quizCount;

    @Column(name = "concept_count")
    private int conceptCount;

    @Builder(access = AccessLevel.PRIVATE)
    private LearningSetStatJpaEntity(Level level, int quizCount, int conceptCount) {
        this.level = level;
        this.quizCount = quizCount;
        this.conceptCount = conceptCount;
    }

    public static LearningSetStatJpaEntity from(LearningSetStat learningSetStat) {
        return LearningSetStatJpaEntity.builder()
                .level(learningSetStat.getLevel())
                .quizCount(learningSetStat.getQuizCount())
                .conceptCount(learningSetStat.getConceptCount())
                .build();
    }

    public LearningSetStat toModel() {
        return LearningSetStat.withId(this.level, this.quizCount, this.conceptCount);
    }
}
