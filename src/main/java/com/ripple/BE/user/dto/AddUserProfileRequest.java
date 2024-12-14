package com.ripple.BE.user.dto;

import com.ripple.BE.user.domain.type.AgeRange;
import com.ripple.BE.user.domain.type.BusinessType;
import com.ripple.BE.user.domain.type.Gender;
import com.ripple.BE.user.domain.type.Job;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record AddUserProfileRequest(
        @NotNull String nickname,
        @NotNull BusinessType businessType,
        @NotNull Job job,
        AgeRange ageRange,
        LocalDateTime birthDay,
        Gender gender,
        String profileIntro,
        Boolean isLearningAlarmAllowed,
        Boolean isCommunityAlarmAllowed) {}
