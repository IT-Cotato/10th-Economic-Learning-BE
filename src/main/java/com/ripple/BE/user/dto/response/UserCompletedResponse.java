package com.ripple.BE.user.dto.response;

import com.ripple.BE.user.dto.UserCompletedDTO;
import lombok.Builder;

@Builder
public record UserCompletedResponse(
        long userId,
        long beginnerCompletedCount,
        long intermediateCompletedCount,
        long advancedCompletedCount,
        long totalConceptCompletedCount,
        long quizCount) {
    public static UserCompletedResponse toUserCompletedResponse(
            final UserCompletedDTO userCompletedDTO) {
        return UserCompletedResponse.builder()
                .userId(userCompletedDTO.userId())
                .beginnerCompletedCount(userCompletedDTO.beginnerCompletedCount())
                .intermediateCompletedCount(userCompletedDTO.intermediateCompletedCount())
                .advancedCompletedCount(userCompletedDTO.advancedCompletedCount())
                .totalConceptCompletedCount(userCompletedDTO.totalConceptCompletedCount())
                .quizCount(userCompletedDTO.quizCount())
                .build();
    }
}
