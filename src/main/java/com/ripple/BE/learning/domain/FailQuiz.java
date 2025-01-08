package com.ripple.BE.learning.domain;

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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "fail_quizzes")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FailQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fail_quiz_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Builder
    public FailQuiz(User user, Quiz quiz) {
        this.user = user;
        this.quiz = quiz;
    }

    public static FailQuiz toFailQuiz(User user, Quiz quiz) {
        return FailQuiz.builder().user(user).quiz(quiz).build();
    }
}
