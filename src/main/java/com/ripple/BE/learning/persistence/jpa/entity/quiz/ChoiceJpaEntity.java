package com.ripple.BE.learning.persistence.jpa.entity.quiz;

import com.ripple.BE.learning.domain.quiz.Choice;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "choices")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChoiceJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "choice_id")
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private QuizJpaEntity quizJpaEntity;

    @Builder(access = AccessLevel.PRIVATE)
    private ChoiceJpaEntity(Long id, String content, QuizJpaEntity quizJpaEntity) {
        this.id = id;
        this.content = content;
        this.quizJpaEntity = quizJpaEntity;
    }

    public Choice toModel() {
        return Choice.withId(id, content);
    }

    public static ChoiceJpaEntity of(Choice choice, QuizJpaEntity quizJpaEntity) {
        return ChoiceJpaEntity.builder()
                .content(choice.getContent())
                .quizJpaEntity(quizJpaEntity)
                .build();
    }
}
