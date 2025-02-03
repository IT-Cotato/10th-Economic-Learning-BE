package com.ripple.BE.user.dto;

import com.ripple.BE.user.domain.type.Gender;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

public record UpdateUserProfileRequest(
        @NotNull String nickname,
        @NotNull String businessType,
        @NotNull String job,
        @NotNull Date birthDate,
        Gender gender,
        String profileIntro,
        Boolean isLearningAlarmAllowed,
        Boolean isCommunityAlarmAllowed,
        Long imageId) {}
