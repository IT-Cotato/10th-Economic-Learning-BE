package com.ripple.BE.learning.domain.quiz;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FailQuiz {

    private final Long id;
    private final Long userId;
    private final Long quizId;

    @Builder(access = AccessLevel.PRIVATE)
    public FailQuiz(Long id, Long userId, Long quizId) {
        this.id = id;
        this.userId = userId;
        this.quizId = quizId;
    }

    public static FailQuiz withoutId(Long userId, Long quizId) {
        return FailQuiz.builder().userId(userId).quizId(quizId).build();
    }

    public static FailQuiz withId(Long id, Long userId, Long quizId) {
        return FailQuiz.builder().id(id).userId(userId).quizId(quizId).build();
    }
}
