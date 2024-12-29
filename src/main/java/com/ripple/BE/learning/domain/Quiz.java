package com.ripple.BE.learning.domain;

import com.ripple.BE.global.entity.BaseEntity;
import com.ripple.BE.learning.domain.type.Purpose;
import com.ripple.BE.learning.domain.type.Type;
import com.ripple.BE.learning.dto.QuizDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "quizzes")
@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Quiz extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "purpose")
    private Purpose purpose; // 용도 - 레벨테스트, 퀴즈

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type; // 형식 - OX, 객관식(단답), 객관식(장문)

    @Size(max = 255)
    @Column(name = "question", nullable = false)
    private String question;

    @Size(max = 255)
    @Column(name = "answer", nullable = false)
    private String answer;

    @Size(max = 255)
    @Column(name = "wrong_answer", nullable = false)
    private String wrongAnswer;

    @Size(max = 255)
    @Column(name = "explanation")
    private String explanation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "learning_set_id")
    private LearningSet learningSet;

    @Builder
    public Quiz(
            Purpose purpose,
            Type type,
            String question,
            String answer,
            String wrongAnswer,
            String explanation,
            LearningSet learningSet) {
        this.purpose = purpose;
        this.type = type;
        this.question = question;
        this.answer = answer;
        this.wrongAnswer = wrongAnswer;
        this.explanation = explanation;
        this.learningSet = learningSet;
    }

    public static Quiz toQuiz(final QuizDTO quizDTO) {
        return Quiz.builder()
                .purpose(quizDTO.purpose())
                .type(quizDTO.type())
                .question(quizDTO.question())
                .answer(quizDTO.answer())
                .wrongAnswer(quizDTO.wrongAnswer())
                .explanation(quizDTO.explanation())
                .build();
    }

    public void setLearningSet(LearningSet learningSet) {
        this.learningSet = learningSet;
        learningSet.getQuizzes().add(this);
    }
}
