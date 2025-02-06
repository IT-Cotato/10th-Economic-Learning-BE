package com.ripple.BE.user.dto.response;

import com.ripple.BE.user.dto.UserGoalDTO;

public record UserGoalResponse(int conceptGoal, int quizGoal, int articleGoal) {
    public static UserGoalResponse toUserGoalResponse(UserGoalDTO userGoalDTO) {
        return new UserGoalResponse(
                userGoalDTO.conceptGoal(), userGoalDTO.quizGoal(), userGoalDTO.articleGoal());
    }
}
