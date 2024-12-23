package com.ripple.BE.learning.dto.response;

import com.ripple.BE.learning.domain.Quiz;
import com.ripple.BE.user.domain.type.Level;
import java.util.List;
import lombok.Builder;

@Builder
public record LevelTestResultResponse(
        Integer correctCount, Level level, List<Explanation> explanations) {
    @Builder
    public record Explanation(Long quizId, String question, String explanation) {
        public static Explanation from(Quiz quiz) {
            return Explanation.builder()
                    .quizId(quiz.getId())
                    .question(quiz.getQuestion())
                    .explanation(quiz.getExplanation())
                    .build();
        }
    }

    public static LevelTestResultResponse from(
            Integer correctCount, Level level, List<Explanation> explanations) {
        return LevelTestResultResponse.builder()
                .correctCount(correctCount)
                .level(level)
                .explanations(explanations)
                .build();
    }
}
