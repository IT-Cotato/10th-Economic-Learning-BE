package com.ripple.BE.learning.dto.response;

import com.ripple.BE.learning.dto.QuizDTO;
import com.ripple.BE.user.domain.type.Level;
import lombok.Builder;

@Builder
public record ScrapQuizResponse(long quizId, String quizName, String learningSetName, Level level) {

    public static ScrapQuizResponse toScrapQuizResponse(QuizDTO quizDTO) {
        return builder()
                .quizId(quizDTO.id())
                .quizName(quizDTO.name())
                .learningSetName(quizDTO.learningSetName())
                .level(quizDTO.level())
                .build();
    }
}
