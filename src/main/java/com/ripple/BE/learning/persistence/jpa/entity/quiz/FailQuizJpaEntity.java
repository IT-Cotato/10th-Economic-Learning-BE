package com.ripple.BE.learning.persistence.jpa.entity.quiz;

import com.ripple.BE.learning.domain.quiz.FailQuiz;
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

@Table(name = "fail_quizzes")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FailQuizJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fail_quiz_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "quiz_id", nullable = false)
    private Long quizId;

    @Builder(access = AccessLevel.PRIVATE)
    private FailQuizJpaEntity(Long id, Long userId, Long quizId) {
        this.id = id;
        this.userId = userId;
        this.quizId = quizId;
    }

    public static FailQuizJpaEntity from(final FailQuiz failQuiz) {
        return FailQuizJpaEntity.builder()
                .id(failQuiz.getId())
                .userId(failQuiz.getUserId())
                .quizId(failQuiz.getQuizId())
                .build();
    }
}
