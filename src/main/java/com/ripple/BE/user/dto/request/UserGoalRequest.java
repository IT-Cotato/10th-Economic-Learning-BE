package com.ripple.BE.user.dto.request;

import jakarta.validation.constraints.Min;

public record UserGoalRequest(
        @Min(value = 1, message = "1 이상의 값을 입력해주세요.") int conceptGoal,
        @Min(value = 1, message = "1 이상의 값을 입력해주세요.") int quizGoal,
        @Min(value = 1, message = "1 이상의 값을 입력해주세요.") int articleGoal) {}
