package com.ripple.BE.user.dto;

import com.ripple.BE.user.dto.request.UserGoalRequest;

public record UserGoalDTO(int conceptGoal, int quizGoal, int articleGoal) {

    public static UserGoalDTO toUserGoalDTO(UserGoalRequest userGoalRequest) {
        return new UserGoalDTO(
                userGoalRequest.conceptGoal(), userGoalRequest.quizGoal(), userGoalRequest.articleGoal());
    }
}
