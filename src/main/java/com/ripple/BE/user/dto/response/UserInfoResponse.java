package com.ripple.BE.user.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ripple.BE.user.domain.type.Level;
import com.ripple.BE.user.dto.UserInfoDTO;
import java.util.Date;

public record UserInfoResponse(
        Long userId,
        String profileImageURL,
        String nickname,
        String profileIntro,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") Date birthDate,
        String businessType,
        String job,
        Long currentStreak,
        Level level,
        Long quizCorrectRate) {

    public static UserInfoResponse toUserInfoResponse(UserInfoDTO userInfoDTO) {
        return new UserInfoResponse(
                userInfoDTO.userId(),
                userInfoDTO.profileImageURL(),
                userInfoDTO.nickname(),
                userInfoDTO.profileIntro(),
                userInfoDTO.birthDate(),
                userInfoDTO.businessType(),
                userInfoDTO.job(),
                userInfoDTO.currentStreak(),
                userInfoDTO.level(),
                userInfoDTO.quizCorrectRate());
    }
}
