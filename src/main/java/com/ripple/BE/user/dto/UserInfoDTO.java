package com.ripple.BE.user.dto;

import com.ripple.BE.user.domain.type.Level;
import java.util.Date;
import lombok.Builder;

@Builder
public record UserInfoDTO(
        Long userId,
        String profileImageURL,
        String nickname,
        String profileIntro,
        Date birthDate,
        String businessType,
        String job,
        Long currentStreak,
        Level level,
        Long quizCorrectRate,
        Boolean isLevelTestCompleted,
        Boolean isAlarmOn) {}
