package com.ripple.BE.user.dto.response;

import com.ripple.BE.user.dto.QuestDTO;

public record QuestResponse(int conceptProgress, int quizProgress, int articleProgress) {

    public static QuestResponse toQuestResponse(QuestDTO questDTO) {
        return new QuestResponse(
                questDTO.conceptProgress(), questDTO.quizProgress(), questDTO.articleProgress());
    }
}
