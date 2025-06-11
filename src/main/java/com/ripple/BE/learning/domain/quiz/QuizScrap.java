package com.ripple.BE.learning.domain.quiz;

import lombok.Builder;
import lombok.Getter;

@Getter
public class QuizScrap {

    private final Long id;
    private final Long userId;
    private final Long quizId;

    @Builder(access = lombok.AccessLevel.PRIVATE)
    private QuizScrap(Long id, Long userId, Long quizId) {
        this.id = id;
        this.userId = userId;
        this.quizId = quizId;
    }

    public static QuizScrap withoutId(Long userId, Long quizId) {
        return QuizScrap.builder().userId(userId).quizId(quizId).build();
    }

    public static QuizScrap withId(Long id, Long userId, Long quizId) {
        return QuizScrap.builder().id(id).userId(userId).quizId(quizId).build();
    }
}
