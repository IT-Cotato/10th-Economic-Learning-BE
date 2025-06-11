package com.ripple.BE.learning.persistence.jpa.entity.quiz;

import com.ripple.BE.global.entity.BaseJpaEntity;
import com.ripple.BE.learning.domain.quiz.Quiz;
import com.ripple.BE.learning.domain.type.Type;
import com.ripple.BE.user.domain.type.Level;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "quizzes")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizJpaEntity extends BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "level")
    private Level level;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type; // 형식 - OX, 객관식(단답), 객관식(장문)

    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 255)
    @Column(name = "question", nullable = false)
    private String question;

    @Size(max = 255)
    @Column(name = "answer", nullable = false)
    private String answer;

    @Column(name = "explanation", nullable = false, columnDefinition = "TEXT")
    private String explanation;

    @Column(name = "learning_set_id")
    private Long learningSetId;

    @Column(name = "learning_set_name")
    private String learningSetName;

    @OneToMany(
            mappedBy = "quizJpaEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<ChoiceJpaEntity> choices;

    @Builder(access = AccessLevel.PRIVATE)
    private QuizJpaEntity(
            Long id,
            Level level,
            Type type,
            String name,
            String question,
            String answer,
            String explanation,
            Long learningSetId,
            String learningSetName,
            List<ChoiceJpaEntity> choices) {
        this.id = id;
        this.level = level;
        this.type = type;
        this.name = name;
        this.question = question;
        this.answer = answer;
        this.explanation = explanation;
        this.learningSetId = learningSetId;
        this.learningSetName = learningSetName;
        this.choices = choices != null ? choices : new ArrayList<>();
    }

    public static QuizJpaEntity from(final Quiz quiz) {
        QuizJpaEntity quizJpaEntity =
                QuizJpaEntity.builder()
                        .id(quiz.getId())
                        .level(quiz.getLevel())
                        .type(quiz.getType())
                        .name(quiz.getName())
                        .question(quiz.getQuestion())
                        .answer(quiz.getAnswer())
                        .explanation(quiz.getExplanation())
                        .learningSetId(quiz.getLearningSetId())
                        .learningSetName(quiz.getLearningSetName())
                        .choices(new ArrayList<>())
                        .build();

        quiz.getChoices()
                .forEach(
                        choice -> {
                            ChoiceJpaEntity choiceJpaEntity = ChoiceJpaEntity.of(choice, quizJpaEntity);
                            quizJpaEntity.getChoices().add(choiceJpaEntity);
                        });

        return quizJpaEntity;
    }

    public Quiz toModel() {
        return Quiz.withId(
                this.id,
                this.level,
                this.type,
                this.name,
                this.question,
                this.answer,
                this.explanation,
                this.learningSetId,
                this.learningSetName,
                this.choices.stream().map(ChoiceJpaEntity::toModel).toList());
    }
}
