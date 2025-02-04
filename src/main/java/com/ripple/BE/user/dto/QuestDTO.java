package com.ripple.BE.user.dto;

public record QuestDTO(Long conceptProgress, Long quizProgress, Long articleProgress) {

    public static QuestDTO toQuestDTO(
            final Long conceptProgress, final Long quizProgress, final Long articleProgress) {
        return new QuestDTO(conceptProgress, quizProgress, articleProgress);
    }
}
