package com.ripple.BE.learning.domain.learningset;

import com.ripple.BE.global.entity.BaseEntity;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.domain.type.Level;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "user_learning_sets")
@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserLearningSet extends BaseEntity { // 학습 세트 완료 여부

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "learning_set_id", nullable = false)
    private LearningSet learningSet;

    private Level level;

    @Column(name = "is_learning_set_completed")
    private boolean isLearningSetCompleted = false;

    @Column(name = "is_concept_completed")
    private boolean isConceptCompleted = false;

    @Column(name = "is_quiz_completed")
    private boolean isQuizCompleted = false;

    public void setConceptCompleted() {
        this.isConceptCompleted = true;

        if (this.isQuizCompleted) {
            this.isLearningSetCompleted = true;
        }
    }

    public void setQuizCompleted() {
        this.isQuizCompleted = true;

        if (this.isConceptCompleted) {
            this.isLearningSetCompleted = true;
        }
    }

    public static UserLearningSet toUserLearningSet(User user, LearningSet learningSet, Level level) {
        return UserLearningSet.builder().user(user).learningSet(learningSet).level(level).build();
    }
}
