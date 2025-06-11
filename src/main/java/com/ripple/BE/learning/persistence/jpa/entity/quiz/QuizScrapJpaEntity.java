package com.ripple.BE.learning.persistence.jpa.entity.quiz;

import com.ripple.BE.learning.domain.quiz.QuizScrap;
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

@Table(name = "quiz_scraps")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizScrapJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "quiz_id")
    private Long quizId;

    @Builder(access = AccessLevel.PRIVATE)
    private QuizScrapJpaEntity(Long id, Long userId, Long quizId) {
        this.id = id;
        this.userId = userId;
        this.quizId = quizId;
    }

    public QuizScrap toModel() {
        return QuizScrap.withId(this.id, this.userId, this.quizId);
    }

    public static QuizScrapJpaEntity from(QuizScrap quizScrap) {
        return QuizScrapJpaEntity.builder()
                .id(quizScrap.getId())
                .userId(quizScrap.getUserId())
                .quizId(quizScrap.getQuizId())
                .build();
    }
}
