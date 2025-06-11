package com.ripple.BE.learning.persistence.jpa.entity.learningset;

import com.ripple.BE.global.entity.BaseJpaEntity;
import com.ripple.BE.learning.domain.learningset.UserLearningSet;
import com.ripple.BE.user.domain.type.Level;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "user_learning_sets")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserLearningSetJpaEntity extends BaseJpaEntity { // 학습 세트 완료 여부

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "learning_set_id", nullable = false)
    private Long learningSetId;

    @Column(name = "learning_set_name", nullable = false)
    private String learningSetName;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    private Level level;

    @Column(name = "is_learning_set_completed")
    private boolean isLearningSetCompleted = false;

    @Column(name = "is_concept_completed")
    private boolean isConceptCompleted = false;

    @Column(name = "is_quiz_completed")
    private boolean isQuizCompleted = false;

    @Builder(access = AccessLevel.PRIVATE)
    private UserLearningSetJpaEntity(
            Long id,
            Long userId,
            Long learningSetId,
            Level level,
            String learningSetName,
            boolean isLearningSetCompleted,
            boolean isConceptCompleted,
            boolean isQuizCompleted) {
        this.id = id;
        this.userId = userId;
        this.learningSetId = learningSetId;
        this.level = level;
        this.learningSetName = learningSetName;
        this.isLearningSetCompleted = isLearningSetCompleted;
        this.isConceptCompleted = isConceptCompleted;
        this.isQuizCompleted = isQuizCompleted;
    }

    public UserLearningSet toModel() {
        return UserLearningSet.withId(
                this.id,
                this.userId,
                this.learningSetId,
                this.learningSetName,
                this.level,
                this.isLearningSetCompleted,
                this.isConceptCompleted,
                this.isQuizCompleted);
    }

    public static UserLearningSetJpaEntity from(UserLearningSet userLearningSet) {
        return UserLearningSetJpaEntity.builder()
                .id(userLearningSet.getId())
                .userId(userLearningSet.getUserId())
                .learningSetId(userLearningSet.getLearningSetId())
                .level(userLearningSet.getLevel())
                .learningSetName(userLearningSet.getLearningSetName())
                .isLearningSetCompleted(userLearningSet.isLearningSetCompleted())
                .isConceptCompleted(userLearningSet.isConceptCompleted())
                .isQuizCompleted(userLearningSet.isQuizCompleted())
                .build();
    }
}
