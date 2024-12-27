package com.ripple.BE.learning.domain;

import com.ripple.BE.global.entity.BaseEntity;
import com.ripple.BE.user.domain.User;
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

@Table(name = "learing_set_complete")
@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LearningSetComplete extends BaseEntity { // 학습 세트 완료 여부

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

    @Column(name = "is_learning_set_completed")
    private boolean isLearningSetCompleted = false; // 학습 세트 완료 여부

    @Column(name = "is_concept_completed")
    private boolean isConceptCompleted = false; // 개념 학습 완료 여부

    @Column(name = "is_quiz_completed")
    private boolean isQuizCompleted = false; // 퀴즈 완료 여부

    // 개념 학습 완료 여부 설정
    public void setConceptCompleted(boolean isConceptCompleted) {
        this.isConceptCompleted = isConceptCompleted;
        if (isConceptCompleted && isQuizCompleted) {
            isLearningSetCompleted = true;
        }
    }

    // 퀴즈 완료 여부 설정
    public void setQuizCompleted(boolean isQuizCompleted) {
        this.isQuizCompleted = isQuizCompleted;
        if (isConceptCompleted && isQuizCompleted) {
            isLearningSetCompleted = true;
        }
    }

    public static LearningSetComplete toLearningSetComplete(User user, LearningSet learningSet) {
        return LearningSetComplete.builder().user(user).learningSet(learningSet).build();
    }
}
