package com.ripple.BE.user.dto.request;

import com.ripple.BE.user.domain.type.Gender;
import java.util.Date;

public record PatchUserProfileRequest(
        String nickname,
        String businessType,
        String job,
        Date birthDate,
        Gender gender,
        String profileIntro,
        Boolean isLearningAlarmAllowed,
        Boolean isCommunityAlarmAllowed,
        Long imageId) {}
