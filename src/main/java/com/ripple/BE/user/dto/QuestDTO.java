package com.ripple.BE.user.dto;

public record QuestDTO(int conceptProgress, int quizProgress, int articleProgress) {

    public static QuestDTO toQuestDTO(
            final int conceptProgress, final int quizProgress, final int articleProgress) {
        return new QuestDTO(conceptProgress, quizProgress, articleProgress);
    }
}
